package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.client.CommentsDisabledException;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.mapper.dto.CommentDtoMapper;
import ca.metricalsky.winston.mapper.entity.CommentMapper;
import ca.metricalsky.winston.service.CommentService;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
public class FetchCommentsActionHandler extends FetchActionHandler<CommentDto> {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    private final CommentDtoMapper commentDtoMapper = Mappers.getMapper(CommentDtoMapper.class);

    private final CommentService commentService;
    private final VideoCommentsService videoCommentsService;
    private final YouTubeService youTubeService;

    public FetchCommentsActionHandler(
            FetchActionService fetchActionService,
            CommentService commentService,
            VideoCommentsService videoCommentsService,
            YouTubeService youTubeService
    ) {
        super(fetchActionService);
        this.commentService = commentService;
        this.videoCommentsService = videoCommentsService;
        this.youTubeService = youTubeService;
    }

    @Override
    protected FetchResult<CommentDto> doFetch(FetchActionEntity fetchAction) {
        try {
            var commentThreadListResponse = youTubeService.getComments(fetchAction);
            var commentEntities = commentThreadListResponse.getItems()
                    .stream()
                    .map(commentMapper::fromYouTube)
                    .toList();
            commentService.saveAll(commentEntities);
            var commentDtos = commentEntities.stream()
                    .map(commentDtoMapper::fromEntity)
                    .toList();
            var nextFetchAction = getNextFetchAction(fetchAction, commentThreadListResponse);
            return new FetchResult<>(fetchAction, commentDtos, nextFetchAction);
        } catch (CommentsDisabledException ex) {
            videoCommentsService.markVideoCommentsDisabled(fetchAction.getObjectId());
            throw ex;
        }
    }

    private static FetchActionEntity getNextFetchAction(FetchActionEntity fetchAction, CommentThreadListResponse youTubeResponse) {
        return youTubeResponse.getNextPageToken() == null ? null : FetchActionEntity.builder()
                .fetchOperationId(fetchAction.getFetchOperationId())
                .actionType(fetchAction.getActionType())
                .objectId(fetchAction.getObjectId())
                .pageToken(youTubeResponse.getNextPageToken())
                .build();
    }
}

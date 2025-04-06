package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.client.CommentsDisabledException;
import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.events.FetchEvent;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.mapper.dto.CommentDtoMapper;
import ca.metricalsky.winston.mapper.entity.CommentMapper;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.service.CommentService;
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
    private final VideoRepository videoRepository;
    private final YouTubeClientAdapter youTubeClientAdapter;

    public FetchCommentsActionHandler(
            FetchActionService fetchActionService,
            CommentService commentService,
            VideoRepository videoRepository,
            YouTubeClientAdapter youTubeClientAdapter
    ) {
        super(fetchActionService);
        this.commentService = commentService;
        this.videoRepository = videoRepository;
        this.youTubeClientAdapter = youTubeClientAdapter;
    }

    @Override
    protected FetchResult<CommentDto> doFetch(FetchAction fetchAction) {
        try {
            var commentThreadListResponse = youTubeClientAdapter.getComments(fetchAction);
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
            videoRepository.findById(fetchAction.getObjectId()).ifPresent(video -> {
                video.setCommentsDisabled(true);
                videoRepository.save(video);
            });
            throw ex;
        }
    }

    private static FetchAction getNextFetchAction(FetchAction fetchAction, CommentThreadListResponse youTubeResponse) {
        return youTubeResponse.getNextPageToken() == null ? null : FetchAction.builder()
                .fetchRequestId(fetchAction.getFetchRequestId())
                .actionType(fetchAction.getActionType())
                .objectId(fetchAction.getObjectId())
                .pageToken(youTubeResponse.getNextPageToken())
                .build();
    }

    @Override
    protected FetchEvent getFetchEvent(FetchResult<CommentDto> fetchResult) {
        var status = fetchResult.hasNextFetchAction() ? FetchStatus.FETCHING : FetchStatus.COMPLETED;
        return FetchEvent.data("fetch-comments", fetchResult.objectId(), status, fetchResult.items());
    }
}

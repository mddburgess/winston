package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.mapper.dto.CommentDtoMapper;
import ca.metricalsky.winston.mapper.entity.CommentMapper;
import ca.metricalsky.winston.service.CommentService;
import com.google.api.services.youtube.model.CommentListResponse;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchRepliesActionHandler implements FetchActionHandler<CommentDto> {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    private final CommentDtoMapper commentDtoMapper = Mappers.getMapper(CommentDtoMapper.class);
    private final CommentService commentService;
    private final YouTubeClientAdapter youTubeClientAdapter;

    @Override
    public FetchResult<CommentDto> fetch(FetchAction fetchAction) {
        var commentListResponse = youTubeClientAdapter.getReplies(fetchAction);
        var replyEntities = commentListResponse.getItems()
                .stream()
                .map(commentMapper::fromYouTube)
                .toList();
        commentService.findById(fetchAction.getObjectId()).ifPresent(topLevelComment -> {
            replyEntities.forEach(replyEntity -> replyEntity.setVideoId(topLevelComment.getVideoId()));
        });
        commentService.saveAll(replyEntities);
        var replyDtos = replyEntities.stream()
                .map(commentDtoMapper::fromEntity)
                .toList();
        var nextFetchAction = getNextFetchAction(fetchAction, commentListResponse);
        return new FetchResult<>(fetchAction, replyDtos, nextFetchAction);
    }

    private static FetchAction getNextFetchAction(FetchAction fetchAction, CommentListResponse youTubeResponse) {
        return youTubeResponse.getNextPageToken() == null ? null : FetchAction.builder()
                .fetchRequestId(fetchAction.getFetchRequestId())
                .actionType(fetchAction.getActionType())
                .objectId(fetchAction.getObjectId())
                .pageToken(youTubeResponse.getNextPageToken())
                .build();
    }
}

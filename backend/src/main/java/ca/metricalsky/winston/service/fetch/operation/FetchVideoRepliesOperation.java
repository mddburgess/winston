package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.repository.CommentRepository;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchVideoRepliesOperation implements FetchOperation<Comment> {

    private final CommentRepository commentRepository;
    private final FetchActionHandler<Comment> fetchRepliesActionHandler;
    private final VideoCommentsService videoCommentsService;

    @Override
    public void fetch(FetchOperationEntity fetchOperation) {
        var videoId = fetchOperation.getObjectId();
        for (var commentId : commentRepository.findIdsMissingRepliesByVideoId(videoId)) {
            var action = getFirstFetchAction(fetchOperation, commentId);
            while (action != null) {
                action = fetchRepliesActionHandler.fetch(action);
            }
        }
    }

    private static FetchActionEntity getFirstFetchAction(FetchOperationEntity fetchOperation, String commentId) {
        return FetchActionEntity.builder()
                .fetchOperationId(fetchOperation.getId())
                .actionType(FetchActionEntity.Type.REPLIES)
                .objectId(commentId)
                .build();
    }

    @Override
    public void afterFetch(FetchOperationEntity fetchOperation) {
        videoCommentsService.updateVideoComments(fetchOperation.getObjectId());
    }
}

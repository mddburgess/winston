package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.domain.PullOperationContext;
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.database.repository.comment.CommentRepository;
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
            var operationContext = PullOperationContext.builder()
                    .operation(fetchOperation)
                    .nextAction(getFirstFetchAction(fetchOperation, commentId))
                    .build();

            while (operationContext.hasNextAction()) {
                fetchRepliesActionHandler.fetch(operationContext);
            }
        }
    }

    private static FetchActionEntity getFirstFetchAction(FetchOperationEntity fetchOperation, String commentId) {
        var fetchAction = new FetchActionEntity();
        fetchAction.setFetchOperationId(fetchOperation.getId());
        fetchAction.setActionType(FetchActionEntity.Type.REPLIES);
        fetchAction.setObjectId(commentId);
        return fetchAction;
    }

    @Override
    public void afterFetch(FetchOperationEntity fetchOperation) {
        videoCommentsService.updateVideoComments(fetchOperation.getObjectId());
    }
}

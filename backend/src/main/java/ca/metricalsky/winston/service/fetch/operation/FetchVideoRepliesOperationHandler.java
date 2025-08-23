package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.FetchStatusEvent;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.FetchOperationException;
import ca.metricalsky.winston.repository.CommentRepository;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchVideoRepliesOperationHandler implements FetchOperationHandler {

    private final CommentRepository commentRepository;
    private final FetchActionHandler<Comment> fetchRepliesActionHandler;
    private final FetchOperationService fetchOperationService;
    private final VideoCommentsService videoCommentsService;
    private final SsePublisher ssePublisher;

    @Override
    public void fetch(FetchOperationEntity fetchOperation) {
        fetchOperation = fetchOperationService.startFetch(fetchOperation);
        ssePublisher.publish(FetchStatusEvent.operation(fetchOperation));
        try {
            var videoId = fetchOperation.getObjectId();
            for (var commentId : commentRepository.findIdsMissingRepliesByVideoId(videoId)) {
                fetchReplies(fetchOperation, commentId);
            }
            fetchOperation = fetchOperationService.fetchSuccessful(fetchOperation);
        } catch (FetchOperationException ex) {
            fetchOperation = fetchOperationService.fetchWarning(fetchOperation, ex.getCause());
        } catch (RuntimeException ex) {
            fetchOperation = fetchOperationService.fetchFailed(fetchOperation, ex);
            throw ex;
        } finally {
            ssePublisher.publish(FetchStatusEvent.operation(fetchOperation));
            afterFetch(fetchOperation);
        }
    }

    private void fetchReplies(FetchOperationEntity fetchOperation, String commentId) {
        var action = getFirstFetchAction(fetchOperation, commentId);
        while (action != null) {
            action = fetchRepliesActionHandler.fetch(action);
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

package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.repository.CommentRepository;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchRepliesActionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchVideoRepliesOperationHandler implements FetchOperationHandler {

    private final CommentRepository commentRepository;
    private final FetchRepliesActionHandler fetchRepliesActionHandler;
    private final FetchOperationService fetchOperationService;
    private final VideoCommentsService videoCommentsService;

    @Override
    public void fetch(FetchOperationEntity operation, SsePublisher ssePublisher) {
        operation = fetchOperationService.startFetch(operation);
        try {
            var videoId = operation.getObjectId();
            for (var commentId : commentRepository.findIdsMissingRepliesByVideoId(videoId)) {
                fetchReplies(operation, commentId, ssePublisher);
            }
            fetchOperationService.fetchSuccessful(operation);
        } catch (RuntimeException ex) {
            fetchOperationService.fetchFailed(operation, ex);
            throw ex;
        } finally {
            afterFetch(operation);
        }
    }

    private void fetchReplies(FetchOperationEntity operation, String commentId, SsePublisher ssePublisher) {
        var action = getFirstFetchAction(operation, commentId);
        while (action != null) {
            action = fetchRepliesActionHandler.fetch(action, ssePublisher);
        }
    }

    private static FetchActionEntity getFirstFetchAction(FetchOperationEntity operation, String commentId) {
        return FetchActionEntity.builder()
                .fetchOperationId(operation.getId())
                .actionType(FetchActionEntity.Type.REPLIES)
                .objectId(commentId)
                .build();
    }

    @Override
    public void afterFetch(FetchOperationEntity operation) {
        videoCommentsService.updateVideoComments(operation.getObjectId());
    }
}

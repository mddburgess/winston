package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.repository.CommentRepository;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchRepliesActionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchVideoRepliesRequestHandler implements FetchRequestHandler {

    private final CommentRepository commentRepository;
    private final FetchRepliesActionHandler fetchRepliesActionHandler;
    private final FetchRequestService fetchRequestService;
    private final VideoCommentsService videoCommentsService;

    @Override
    public void fetch(FetchRequestEntity fetchRequest, SsePublisher ssePublisher) {
        fetchRequest = fetchRequestService.startFetch(fetchRequest);
        try {
            var videoId = fetchRequest.getObjectId();
            for (var commentId : commentRepository.findIdsMissingRepliesByVideoId(videoId)) {
                fetchReplies(fetchRequest, commentId, ssePublisher);
            }
            fetchRequestService.fetchCompleted(fetchRequest);
        } catch (RuntimeException ex) {
            fetchRequestService.fetchFailed(fetchRequest, ex);
            throw ex;
        } finally {
            afterFetch(fetchRequest);
        }
    }

    private void fetchReplies(FetchRequestEntity fetchRequest, String commentId, SsePublisher ssePublisher) {
        var fetchAction = getFirstFetchAction(fetchRequest, commentId);
        while (fetchAction != null) {
            fetchAction = fetchRepliesActionHandler.fetch(fetchAction, ssePublisher);
        }
    }

    private static FetchActionEntity getFirstFetchAction(FetchRequestEntity fetchRequest, String commentId) {
        return FetchActionEntity.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(FetchActionEntity.ActionType.REPLIES)
                .objectId(commentId)
                .build();
    }

    @Override
    public void afterFetch(FetchRequestEntity fetchRequest) {
        videoCommentsService.updateVideoComments(fetchRequest.getObjectId());
    }
}

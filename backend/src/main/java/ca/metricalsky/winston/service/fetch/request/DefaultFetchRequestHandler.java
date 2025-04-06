package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.events.PublisherException;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandlerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultFetchRequestHandler implements FetchRequestHandler {

    private final FetchActionHandlerFactory fetchActionHandlerFactory;
    private final FetchRequestService fetchRequestService;

    @Override
    public void fetch(FetchRequest fetchRequest, SsePublisher ssePublisher) {
        fetchRequest = fetchRequestService.startFetch(fetchRequest);
        var fetchAction = getFirstFetchAction(fetchRequest);
        try {
            while (fetchAction != null) {
                var actionHandler = fetchActionHandlerFactory.getHandlerForAction(fetchAction);
                fetchAction = actionHandler.fetch(fetchAction, ssePublisher);
            }
            fetchRequestService.fetchCompleted(fetchRequest);
        } catch (PublisherException ex) {
            if (fetchAction == null) {
                fetchRequestService.fetchCompleted(fetchRequest);
            } else {
                fetchRequestService.fetchFailed(fetchRequest, ex);
            }
            throw ex;
        } catch (RuntimeException ex) {
            fetchRequestService.fetchFailed(fetchRequest, ex);
            throw ex;
        }
    }

    private static FetchAction getFirstFetchAction(FetchRequest fetchRequest) {
        return FetchAction.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(FetchAction.ActionType.valueOf(fetchRequest.getFetchType().name()))
                .objectId(fetchRequest.getObjectId())
                .publishedAfter(fetchRequest.getPublishedAfter())
                .publishedBefore(fetchRequest.getPublishedBefore())
                .status(FetchAction.Status.FETCHING)
                .build();
    }
}

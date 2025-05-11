package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class DefaultFetchRequestHandler implements FetchRequestHandler {

    private final FetchRequestService fetchRequestService;

    @Override
    public void fetch(FetchRequest fetchRequest, SsePublisher ssePublisher) {
        fetchRequest = fetchRequestService.startFetch(fetchRequest);
        var fetchAction = getFirstFetchAction(fetchRequest);
        try {
            while (fetchAction != null) {
                var actionHandler = getFetchActionHandler();
                fetchAction = actionHandler.fetch(fetchAction, ssePublisher);
            }
            fetchRequestService.fetchCompleted(fetchRequest);
        } catch (RuntimeException ex) {
            fetchRequestService.fetchFailed(fetchRequest, ex);
            throw ex;
        }
    }

    protected abstract FetchActionHandler<?> getFetchActionHandler();

    private static FetchAction getFirstFetchAction(FetchRequest fetchRequest) {
        return FetchAction.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(FetchAction.ActionType.valueOf(fetchRequest.getFetchType().name()))
                .objectId(fetchRequest.getObjectId())
                .publishedAfter(fetchRequest.getPublishedAfter())
                .publishedBefore(fetchRequest.getPublishedBefore())
                .build();
    }
}

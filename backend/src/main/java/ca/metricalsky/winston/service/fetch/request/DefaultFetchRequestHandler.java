package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class DefaultFetchRequestHandler implements FetchRequestHandler {

    private final FetchRequestService fetchRequestService;

    @Override
    public void fetch(FetchRequestEntity fetchRequest, SsePublisher ssePublisher) {
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
        } finally {
            afterFetch(fetchRequest);
        }
    }

    protected abstract FetchActionHandler<?> getFetchActionHandler();

    private static FetchActionEntity getFirstFetchAction(FetchRequestEntity fetchRequest) {
        return FetchActionEntity.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(FetchActionEntity.ActionType.valueOf(fetchRequest.getFetchType().name()))
                .objectId(fetchRequest.getObjectId())
                .publishedAfter(fetchRequest.getPublishedAfter())
                .publishedBefore(fetchRequest.getPublishedBefore())
                .build();
    }
}

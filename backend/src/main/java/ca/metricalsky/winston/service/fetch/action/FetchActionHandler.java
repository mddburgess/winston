package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.events.EventPublisher;
import ca.metricalsky.winston.events.PublisherException;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchActionHandler<T> {

    private final EventPublisher eventPublisher;
    private final FetchActionService fetchActionService;
    private final FetchAction<T> delegate;

    public FetchActionEntity fetch(FetchActionEntity fetchAction) {
        FetchActionEntity nextFetchAction = null;
        try {
            var fetchResult = fetchInternal(fetchAction);
            nextFetchAction = fetchResult.nextFetchAction();
            eventPublisher.publishEvent(fetchResult);
            return nextFetchAction;
        } catch (PublisherException ex) {
            if (nextFetchAction == null) {
                return null;
            }
            fetchActionService.actionReady(nextFetchAction);
            throw ex;
        }
    }

    private FetchResult<T> fetchInternal(FetchActionEntity fetchAction) {
        try {
            fetchAction = fetchActionService.actionFetching(fetchAction);
            var fetchResult = delegate.fetch(fetchAction);
            fetchActionService.actionSuccessful(fetchAction, fetchResult.items().size());
            return fetchResult;
        } catch (RuntimeException ex) {
            fetchActionService.actionFailed(fetchAction, ex);
            throw ex;
        }
    }
}

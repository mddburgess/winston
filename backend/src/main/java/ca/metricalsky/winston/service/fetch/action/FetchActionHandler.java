package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.events.PublisherException;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class FetchActionHandler<T> {

    private final FetchActionService fetchActionService;
    private final SsePublisher ssePublisher;

    public FetchActionEntity fetch(FetchActionEntity fetchAction) {
        FetchActionEntity nextFetchAction = null;
        try {
            var fetchResult = fetchInternal(fetchAction);
            nextFetchAction = fetchResult.nextFetchAction();
            ssePublisher.publish(fetchResult);
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
            var fetchResult = doFetch(fetchAction);
            fetchActionService.actionSuccessful(fetchAction, fetchResult.items().size());
            return fetchResult;
        } catch (RuntimeException ex) {
            fetchActionService.actionFailed(fetchAction, ex);
            throw ex;
        }
    }

    protected abstract FetchResult<T> doFetch(FetchActionEntity fetchAction);
}

package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.events.FetchDataEvent;
import ca.metricalsky.winston.events.PublisherException;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class FetchActionHandler<T> {

    private final FetchActionService fetchActionService;

    public FetchAction fetch(FetchAction fetchAction, SsePublisher ssePublisher) {
        FetchAction nextFetchAction = null;
        try {
            var fetchResult = fetch(fetchAction);
            nextFetchAction = fetchResult.nextFetchAction();
            ssePublisher.publish(FetchDataEvent.of(fetchResult));
            return nextFetchAction;
        } catch (PublisherException ex) {
            if (nextFetchAction == null) {
                return null;
            }
            fetchActionService.actionReady(nextFetchAction);
            throw ex;
        }
    }

    private FetchResult<T> fetch(FetchAction fetchAction) {
        try {
            fetchAction = fetchActionService.actionFetching(fetchAction);
            var fetchResult = doFetch(fetchAction);
            fetchActionService.actionCompleted(fetchAction, fetchResult.items().size());
            return fetchResult;
        } catch (RuntimeException ex) {
            fetchActionService.actionFailed(fetchAction, ex);
            throw ex;
        }
    }

    protected abstract FetchResult<T> doFetch(FetchAction fetchAction);
}

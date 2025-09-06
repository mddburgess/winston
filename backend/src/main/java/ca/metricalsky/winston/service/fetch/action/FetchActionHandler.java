package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.domain.PullOperationContext;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.events.EventPublisher;
import ca.metricalsky.winston.events.PublisherException;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchActionHandler<T> {

    private final EventPublisher eventPublisher;
    private final FetchActionService fetchActionService;
    private final FetchAction<T> delegate;

    public void fetch(PullOperationContext operationContext) {
        try {
            var fetchResult = fetchInternal(operationContext.getNextAction());
            operationContext.setResults(fetchResult.items());
            operationContext.setNextAction(fetchResult.nextFetchAction());
            eventPublisher.publishEvent(operationContext);
        } catch (PublisherException ex) {
            if (operationContext.getNextAction() == null) {
                return;
            }
            fetchActionService.actionReady(operationContext.getNextAction());
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

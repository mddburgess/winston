package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.EventPublisher;
import ca.metricalsky.winston.exception.FetchOperationException;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchOperationHandler<T> {

    private final EventPublisher eventPublisher;
    private final FetchOperationService fetchOperationService;
    private final FetchOperation<T> delegate;

    public void fetch(FetchOperationEntity fetchOperation) {
        fetchOperation = fetchOperationService.startFetch(fetchOperation);
        eventPublisher.publishEvent(fetchOperation);
        try {
            delegate.fetch(fetchOperation);
            fetchOperation = fetchOperationService.fetchSuccessful(fetchOperation);
            eventPublisher.publishEvent(fetchOperation);
        } catch (FetchOperationException ex) {
            fetchOperation = fetchOperationService.fetchWarning(fetchOperation, ex.getCause());
            eventPublisher.publishEvent(fetchOperation, ex.getCause());
        } catch (RuntimeException ex) {
            fetchOperation = fetchOperationService.fetchFailed(fetchOperation, ex);
            eventPublisher.publishEvent(fetchOperation, ex);
            throw ex;
        } finally {
            delegate.afterFetch(fetchOperation);
        }
    }
}

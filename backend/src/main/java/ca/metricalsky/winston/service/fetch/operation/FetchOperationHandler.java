package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.exception.FetchOperationException;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchOperationHandler<T> {

    private final FetchOperationService fetchOperationService;
    private final FetchOperation<T> delegate;

    public void fetch(FetchOperationEntity fetchOperation) {
        try {
            fetchOperation = fetchOperationService.startFetch(fetchOperation);
            delegate.fetch(fetchOperation);
            fetchOperation = fetchOperationService.fetchSuccessful(fetchOperation);
        } catch (FetchOperationException ex) {
            fetchOperation = fetchOperationService.fetchWarning(fetchOperation, ex.getCause());
        } catch (RuntimeException ex) {
            fetchOperation = fetchOperationService.fetchFailed(fetchOperation, ex);
            throw ex;
        } finally {
            delegate.afterFetch(fetchOperation);
        }
    }
}

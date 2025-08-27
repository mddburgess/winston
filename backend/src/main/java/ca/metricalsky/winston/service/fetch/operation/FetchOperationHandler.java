package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.exception.FetchOperationException;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
public class FetchOperationHandler<T> {

    private final FetchOperationService fetchOperationService;
    private final FetchOperation<T> delegate;

    public void fetch(
            @NonNull FetchOperationEntity fetchOperationEntity
    ) {
        try {
            fetchOperationEntity = fetchOperationService.startFetch(fetchOperationEntity);
            delegate.fetch(fetchOperationEntity);
            fetchOperationEntity = fetchOperationService.fetchSuccessful(fetchOperationEntity);
        } catch (FetchOperationException ex) {
            fetchOperationEntity = fetchOperationService.fetchWarning(fetchOperationEntity, ex.getCause());
        } catch (RuntimeException ex) {
            fetchOperationEntity = fetchOperationService.fetchFailed(fetchOperationEntity, ex);
            throw ex;
        } finally {
            delegate.afterFetch(fetchOperationEntity);
        }
    }
}

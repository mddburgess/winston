package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Status;
import ca.metricalsky.winston.events.EventPublisher;
import ca.metricalsky.winston.repository.fetch.FetchOperationRepository;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchOperationService {

    private final EventPublisher eventPublisher;
    private final FetchOperationRepository fetchOperationRepository;

    public FetchOperationEntity startFetch(FetchOperationEntity fetchOperation) {
        return saveAndPublish(fetchOperation, Status.FETCHING, null);
    }

    public FetchOperationEntity fetchSuccessful(FetchOperationEntity fetchOperation) {
        return saveAndPublish(fetchOperation, Status.SUCCESSFUL, null);
    }

    public FetchOperationEntity fetchWarning(FetchOperationEntity fetchOperation, Throwable throwable) {
        return saveAndPublish(fetchOperation, Status.WARNING, throwable);
    }

    public FetchOperationEntity fetchFailed(FetchOperationEntity fetchOperation, Throwable throwable) {
        return saveAndPublish(fetchOperation, Status.FAILED, throwable);
    }

    private FetchOperationEntity saveAndPublish(
            FetchOperationEntity fetchOperation,
            Status fetchOperationStatus,
            Throwable throwable
    ) {
        fetchOperation.setStatus(fetchOperationStatus);
        fetchOperation.setError(throwable == null ? null : Throwables.getStackTraceAsString(throwable));
        fetchOperation = fetchOperationRepository.save(fetchOperation);
        eventPublisher.publishEvent(fetchOperation, throwable);
        return fetchOperation;
    }
}

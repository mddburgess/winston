package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Status;
import ca.metricalsky.winston.events.EventPublisher;
import ca.metricalsky.winston.repository.fetch.FetchOperationRepository;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchOperationService {

    private final EventPublisher eventPublisher;
    private final FetchOperationRepository fetchOperationRepository;

    public FetchOperationEntity startFetch(
            @NonNull FetchOperationEntity fetchOperationEntity
    ) {
        return saveAndPublish(fetchOperationEntity, Status.FETCHING, null);
    }

    public FetchOperationEntity fetchSuccessful(
            @NonNull FetchOperationEntity fetchOperationEntity
    ) {
        return saveAndPublish(fetchOperationEntity, Status.SUCCESSFUL, null);
    }

    public FetchOperationEntity fetchWarning(
            @NonNull FetchOperationEntity fetchOperationEntity,
            @NonNull Throwable throwable
    ) {
        return saveAndPublish(fetchOperationEntity, Status.WARNING, throwable);
    }

    public FetchOperationEntity fetchFailed(
            @NonNull FetchOperationEntity fetchOperationEntity,
            @NonNull Throwable throwable
    ) {
        return saveAndPublish(fetchOperationEntity, Status.FAILED, throwable);
    }

    private FetchOperationEntity saveAndPublish(
            @NonNull FetchOperationEntity fetchOperationEntity,
            @NonNull Status fetchOperationStatus,
            Throwable throwable
    ) {
        fetchOperationEntity.setStatus(fetchOperationStatus);
        fetchOperationEntity.setError(throwable == null ? null : Throwables.getStackTraceAsString(throwable));
        fetchOperationEntity = fetchOperationRepository.save(fetchOperationEntity);
        eventPublisher.publishEvent(fetchOperationEntity, throwable);
        return fetchOperationEntity;
    }
}

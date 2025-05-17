package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Status;
import ca.metricalsky.winston.repository.fetch.FetchOperationRepository;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchOperationService {

    private final FetchOperationRepository fetchOperationRepository;

    public FetchOperationEntity startFetch(FetchOperationEntity operation) {
        operation.setStatus(Status.FETCHING);
        return fetchOperationRepository.save(operation);
    }

    public FetchOperationEntity fetchSuccessful(FetchOperationEntity operation) {
        operation.setStatus(Status.SUCCESSFUL);
        return fetchOperationRepository.save(operation);
    }

    public FetchOperationEntity fetchFailed(FetchOperationEntity operation, Throwable throwable) {
        operation.setStatus(Status.FAILED);
        operation.setError(Throwables.getStackTraceAsString(throwable));
        return fetchOperationRepository.save(operation);
    }
}

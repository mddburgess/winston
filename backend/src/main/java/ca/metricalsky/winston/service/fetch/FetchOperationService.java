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

    public FetchOperationEntity startFetch(FetchOperationEntity fetchOperation) {
        fetchOperation.setStatus(Status.FETCHING);
        return fetchOperationRepository.save(fetchOperation);
    }

    public FetchOperationEntity fetchSuccessful(FetchOperationEntity fetchOperation) {
        fetchOperation.setStatus(Status.SUCCESSFUL);
        return fetchOperationRepository.save(fetchOperation);
    }

    public FetchOperationEntity fetchFailed(FetchOperationEntity fetchOperation, Throwable throwable) {
        fetchOperation.setStatus(Status.FAILED);
        fetchOperation.setError(Throwables.getStackTraceAsString(throwable));
        return fetchOperationRepository.save(fetchOperation);
    }
}

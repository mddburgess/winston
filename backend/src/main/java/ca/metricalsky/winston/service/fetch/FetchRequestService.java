package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity.Status;
import ca.metricalsky.winston.repository.fetch.FetchRequestRepository;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchRequestService {

    private final FetchRequestRepository fetchRequestRepository;

    public FetchRequestEntity startFetch(FetchRequestEntity fetchRequest) {
        fetchRequest.setStatus(Status.FETCHING);
        return fetchRequestRepository.save(fetchRequest);
    }

    public FetchRequestEntity fetchCompleted(FetchRequestEntity fetchRequest) {
        fetchRequest.setStatus(Status.COMPLETED);
        return fetchRequestRepository.save(fetchRequest);
    }

    public FetchRequestEntity fetchFailed(FetchRequestEntity fetchRequest, Throwable throwable) {
        fetchRequest.setStatus(Status.FAILED);
        fetchRequest.setError(Throwables.getStackTraceAsString(throwable));
        return fetchRequestRepository.save(fetchRequest);
    }
}

package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.entity.fetch.FetchRequest.Status;
import ca.metricalsky.winston.repository.fetch.FetchRequestRepository;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchRequestService {

    private final FetchRequestRepository fetchRequestRepository;

    public FetchRequest startFetch(FetchRequest fetchRequest) {
        fetchRequest.setStatus(Status.FETCHING);
        return fetchRequestRepository.save(fetchRequest);
    }

    public FetchRequest fetchCompleted(FetchRequest fetchRequest) {
        fetchRequest.setStatus(Status.COMPLETED);
        return fetchRequestRepository.save(fetchRequest);
    }

    public FetchRequest fetchFailed(FetchRequest fetchRequest, Throwable throwable) {
        fetchRequest.setStatus(Status.FAILED);
        fetchRequest.setError(Throwables.getStackTraceAsString(throwable));
        return fetchRequestRepository.save(fetchRequest);
    }
}

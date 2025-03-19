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

    public void acceptFetch(FetchVideosContext context) {
        var fetchRequest = new FetchRequest();
        fetchRequest.setFetchId(context.getChannelId());
        fetchRequest.setStatus(Status.ACCEPTED);

        fetchRequest = fetchRequestRepository.save(fetchRequest);
        context.setFetchRequest(fetchRequest);
    }

    public void startFetch(FetchVideosContext context) {
        var fetchRequest = context.getFetchRequest();
        fetchRequest.setStatus(Status.FETCHING);

        fetchRequest = fetchRequestRepository.save(fetchRequest);
        context.setFetchRequest(fetchRequest);
    }

    public void completeFetch(FetchVideosContext context) {
        var fetchRequest = context.getFetchRequest();
        fetchRequest.setStatus(Status.COMPLETED);

        fetchRequest = fetchRequestRepository.save(fetchRequest);
        context.setFetchRequest(fetchRequest);
    }

    public void failFetch(FetchVideosContext context, Throwable throwable) {
        var fetchRequest = context.getFetchRequest();
        fetchRequest.setStatus(Status.FAILED);
        fetchRequest.setError(Throwables.getStackTraceAsString(throwable));

        fetchRequest = fetchRequestRepository.save(fetchRequest);
        context.setFetchRequest(fetchRequest);
    }
}

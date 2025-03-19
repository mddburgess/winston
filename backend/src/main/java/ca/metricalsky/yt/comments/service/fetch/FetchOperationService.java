package ca.metricalsky.yt.comments.service.fetch;

import ca.metricalsky.yt.comments.entity.fetch.FetchOperation;
import ca.metricalsky.yt.comments.entity.fetch.FetchOperation.Status;
import ca.metricalsky.yt.comments.repository.fetch.FetchOperationRepository;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchOperationService {

    private final FetchOperationRepository fetchOperationRepository;

    public void startOperation(FetchVideosContext context) {
        var fetchOperation = new FetchOperation();
        fetchOperation.setFetchRequestId(context.getFetchRequest().getId());
        fetchOperation.setChannelId(context.getChannelId());
        fetchOperation.setLastPublishedAt(context.getLastPublishedAt());
        fetchOperation.setNextPageToken(context.getNextPageToken());
        fetchOperation.setStatus(Status.FETCHING);

        fetchOperation = fetchOperationRepository.save(fetchOperation);
        context.setCurrentOperation(fetchOperation);
    }

    public void completeOperation(FetchVideosContext context) {
        var fetchOperation = context.getCurrentOperation();
        fetchOperation.setStatus(Status.COMPLETED);

        fetchOperation = fetchOperationRepository.save(fetchOperation);
        context.setCurrentOperation(fetchOperation);
    }

    public void failOperation(FetchVideosContext context, Throwable throwable) {
        var fetchOperation = context.getCurrentOperation();
        fetchOperation.setStatus(Status.FAILED);
        fetchOperation.setError(Throwables.getStackTraceAsString(throwable));

        fetchOperation = fetchOperationRepository.save(fetchOperation);
        context.setCurrentOperation(fetchOperation);
    }
}

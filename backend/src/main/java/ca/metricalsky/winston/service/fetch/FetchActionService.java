package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity.Status;
import ca.metricalsky.winston.repository.fetch.FetchActionRepository;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchActionService {

    private final FetchActionRepository fetchActionRepository;

    public FetchActionEntity actionReady(FetchActionEntity fetchAction) {
        fetchAction.setStatus(Status.READY);
        return fetchActionRepository.save(fetchAction);
    }

    public FetchActionEntity actionFetching(FetchActionEntity fetchAction) {
        fetchAction.setStatus(Status.FETCHING);
        return fetchActionRepository.save(fetchAction);
    }

    public FetchActionEntity actionSuccessful(FetchActionEntity fetchAction, Integer itemCount) {
        fetchAction.setStatus(Status.SUCCESSFUL);
        fetchAction.setItemCount(itemCount);
        return fetchActionRepository.save(fetchAction);
    }

    public FetchActionEntity actionFailed(FetchActionEntity fetchAction, Throwable throwable) {
        fetchAction.setStatus(Status.FAILED);
        fetchAction.setError(Throwables.getStackTraceAsString(throwable));
        return fetchActionRepository.save(fetchAction);
    }
}

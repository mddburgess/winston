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

    public FetchActionEntity actionReady(FetchActionEntity action) {
        action.setStatus(Status.READY);
        return fetchActionRepository.save(action);
    }

    public FetchActionEntity actionFetching(FetchActionEntity action) {
        action.setStatus(Status.FETCHING);
        return fetchActionRepository.save(action);
    }

    public FetchActionEntity actionSuccessful(FetchActionEntity action, Integer itemCount) {
        action.setStatus(Status.SUCCESSFUL);
        action.setItemCount(itemCount);
        return fetchActionRepository.save(action);
    }

    public FetchActionEntity actionFailed(FetchActionEntity action, Throwable throwable) {
        action.setStatus(Status.FAILED);
        action.setError(Throwables.getStackTraceAsString(throwable));
        return fetchActionRepository.save(action);
    }
}

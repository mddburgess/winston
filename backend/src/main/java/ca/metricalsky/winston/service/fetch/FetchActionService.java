package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchAction.ActionType;
import ca.metricalsky.winston.entity.fetch.FetchAction.Status;
import ca.metricalsky.winston.repository.fetch.FetchActionRepository;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchActionService {

    private final FetchActionRepository fetchActionRepository;

    public FetchAction startAction(FetchContext fetchContext, ActionType actionType) {
        var fetchAction = new FetchAction();
        fetchAction.setFetchRequestId(fetchContext.getFetchRequest().getId());
        fetchAction.setActionType(actionType);
        fetchAction.setObjectId(fetchContext.getObjectId());
        fetchAction.setPublishedAfter(fetchContext.getPublishedAfter());
        fetchAction.setPublishedBefore(fetchContext.getPublishedBefore());
        fetchAction.setPageToken(fetchContext.getPageToken());
        fetchAction.setStatus(Status.FETCHING);
        return fetchActionRepository.save(fetchAction);
    }

    public FetchAction actionCompleted(FetchAction fetchAction, Integer itemCount) {
        fetchAction.setStatus(Status.COMPLETED);
        fetchAction.setItemCount(itemCount);
        return fetchActionRepository.save(fetchAction);
    }

    public FetchAction actionFailed(FetchAction fetchAction, Throwable throwable) {
        fetchAction.setStatus(Status.FAILED);
        fetchAction.setError(Throwables.getStackTraceAsString(throwable));
        return fetchActionRepository.save(fetchAction);
    }
}

package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;

import java.util.Collection;
import java.util.List;

public record FetchResult<T>(
        FetchActionEntity.Type actionType,
        String objectId,
        Collection<T> items,
        FetchActionEntity nextFetchAction
) {

    public FetchResult(FetchActionEntity fetchAction, T item, FetchActionEntity nextFetchAction) {
        this(fetchAction.getActionType(), fetchAction.getObjectId(), List.of(item), nextFetchAction);
    }

    public FetchResult(FetchActionEntity fetchAction, List<T> item, FetchActionEntity nextFetchAction) {
        this(fetchAction.getActionType(), fetchAction.getObjectId(), item, nextFetchAction);
    }

    public boolean hasNextFetchAction() {
        return nextFetchAction != null;
    }
}

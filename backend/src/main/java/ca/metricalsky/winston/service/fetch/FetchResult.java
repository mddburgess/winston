package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchAction;

import java.util.Collection;
import java.util.List;

public record FetchResult<T>(
        FetchAction.ActionType actionType,
        String objectId,
        Collection<T> items,
        FetchAction nextFetchAction
) {

    public FetchResult(FetchAction fetchAction, T item, FetchAction nextFetchAction) {
        this(fetchAction.getActionType(), fetchAction.getObjectId(), List.of(item), nextFetchAction);
    }

    public FetchResult(FetchAction fetchAction, List<T> item, FetchAction nextFetchAction) {
        this(fetchAction.getActionType(), fetchAction.getObjectId(), item, nextFetchAction);
    }

    public boolean hasNextFetchAction() {
        return nextFetchAction != null;
    }
}

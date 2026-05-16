package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.domain.PullOperationContext;
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicFetchOperation<T> implements FetchOperation<T> {

    private final FetchActionHandler<T> fetchActionHandler;

    @Override
    public void fetch(FetchOperationEntity fetchOperation) {
        var operationContext = PullOperationContext.builder()
                .operation(fetchOperation)
                .nextAction(getFirstFetchAction(fetchOperation))
                .build();

        while (operationContext.hasNextAction()) {
            fetchActionHandler.fetch(operationContext);
        }
    }

    FetchActionEntity getFirstFetchAction(FetchOperationEntity fetchOperation) {
        var fetchAction = new FetchActionEntity();
        fetchAction.setFetchOperationId(fetchOperation.getId());
        fetchAction.setActionType(FetchActionEntity.Type.valueOf(fetchOperation.getOperationType().name()));
        fetchAction.setObjectId(fetchOperation.getObjectId());
        fetchAction.setPublishedAfter(fetchOperation.getPublishedAfter());
        fetchAction.setPublishedBefore(fetchOperation.getPublishedBefore());
        return fetchAction;
    }
}

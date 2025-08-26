package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicFetchOperation<T> implements FetchOperation<T> {

    private final FetchActionHandler<T> fetchActionHandler;

    @Override
    public void fetch(FetchOperationEntity fetchOperation) {
        var action = getFirstFetchAction(fetchOperation);
        while (action != null) {
            action = fetchActionHandler.fetch(action);
        }
    }

    private static FetchActionEntity getFirstFetchAction(FetchOperationEntity fetchOperation) {
        return FetchActionEntity.builder()
                .fetchOperationId(fetchOperation.getId())
                .actionType(FetchActionEntity.Type.valueOf(fetchOperation.getOperationType().name()))
                .objectId(fetchOperation.getObjectId())
                .publishedAfter(fetchOperation.getPublishedAfter())
                .publishedBefore(fetchOperation.getPublishedBefore())
                .build();
    }
}

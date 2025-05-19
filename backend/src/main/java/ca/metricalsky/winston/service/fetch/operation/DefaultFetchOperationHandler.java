package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class DefaultFetchOperationHandler implements FetchOperationHandler {

    private final FetchOperationService fetchOperationService;

    @Override
    public void fetch(FetchOperationEntity operation, SsePublisher ssePublisher) {
        operation = fetchOperationService.startFetch(operation);
        var action = getFirstFetchAction(operation);
        try {
            while (action != null) {
                var actionHandler = getFetchActionHandler();
                action = actionHandler.fetch(action, ssePublisher);
            }
            fetchOperationService.fetchSuccessful(operation);
        } catch (RuntimeException ex) {
            fetchOperationService.fetchFailed(operation, ex);
            throw ex;
        } finally {
            afterFetch(operation);
        }
    }

    protected abstract FetchActionHandler<?> getFetchActionHandler();

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

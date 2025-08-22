package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.FetchStatusEvent;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.FetchOperationException;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class DefaultFetchOperationHandler implements FetchOperationHandler {

    private final FetchOperationService fetchOperationService;

    @Override
    public void fetch(FetchOperationEntity fetchOperation, SsePublisher ssePublisher) {
        fetchOperation = fetchOperationService.startFetch(fetchOperation);
        ssePublisher.publish(FetchStatusEvent.operation(fetchOperation));
        var action = getFirstFetchAction(fetchOperation);
        try {
            while (action != null) {
                var actionHandler = getFetchActionHandler();
                action = actionHandler.fetch(action, ssePublisher);
            }
            fetchOperation = fetchOperationService.fetchSuccessful(fetchOperation);
        } catch (FetchOperationException ex) {
            fetchOperation = fetchOperationService.fetchWarning(fetchOperation, ex.getCause());
        } catch (RuntimeException ex) {
            fetchOperation = fetchOperationService.fetchFailed(fetchOperation, ex);
            throw ex;
        } finally {
            ssePublisher.publish(FetchStatusEvent.operation(fetchOperation));
            afterFetch(fetchOperation);
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

package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.SsePublisher;

public interface FetchOperationHandler {

    void fetch(FetchOperationEntity fetchOperation, SsePublisher ssePublisher);

    default void afterFetch(FetchOperationEntity fetchOperation) {
        
    }
}

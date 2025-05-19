package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.SsePublisher;

public interface FetchOperationHandler {

    void fetch(FetchOperationEntity operation, SsePublisher ssePublisher);

    default void afterFetch(FetchOperationEntity operation) {
        
    }
}

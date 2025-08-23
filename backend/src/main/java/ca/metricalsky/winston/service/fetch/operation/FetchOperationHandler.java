package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;

public interface FetchOperationHandler {

    void fetch(FetchOperationEntity fetchOperation);

    default void afterFetch(FetchOperationEntity fetchOperation) {
        
    }
}

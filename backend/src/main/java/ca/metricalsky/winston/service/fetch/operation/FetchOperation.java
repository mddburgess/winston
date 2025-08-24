package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;

public interface FetchOperation<T> {

    void fetch(FetchOperationEntity fetchOperation);

    default void afterFetch(FetchOperationEntity fetchOperation) {
        
    }
}

package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.events.SsePublisher;

public interface FetchRequestHandler {

    void fetch(FetchRequestEntity fetchRequest, SsePublisher ssePublisher);

    default void afterFetch(FetchRequestEntity fetchRequest) {
        
    }
}

package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.events.SsePublisher;

public interface FetchRequestHandler {

    void fetch(FetchRequest fetchRequest, SsePublisher ssePublisher);
}

package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface FetchRequestHandler {

    void fetch(FetchRequest fetchRequest, SseEmitter emitter);
}

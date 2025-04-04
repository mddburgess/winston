package ca.metricalsky.winston.utils;

import ca.metricalsky.winston.events.FetchEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public record SsePublisher(SseEmitter sseEmitter) {

    public void complete() {
        sseEmitter.complete();
    }

    public void completeWithError(Throwable ex) {
        sseEmitter.completeWithError(ex);
    }

    public void publish(FetchEvent fetchEvent) {
        publish(fetchEvent.type(), SseEmitter.event()
                .id(UUID.randomUUID().toString())
                .name(fetchEvent.type())
                .data(fetchEvent, MediaType.APPLICATION_JSON));
    }

    public void publish(ProblemDetail error) {
        publish("error", SseEmitter.event()
                .id(UUID.randomUUID().toString())
                .name("error")
                .data(error, MediaType.APPLICATION_PROBLEM_JSON));
    }

    private void publish(String name, SseEmitter.SseEventBuilder builder) {
        try {
            sseEmitter.send(builder);
        } catch (IOException ex) {
            log.error("Failed to publish SSE event name='{}'", name, ex);
            throw new RuntimeException(ex);
        }
    }
}

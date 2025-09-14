package ca.metricalsky.winston.events;

import ca.metricalsky.winston.events.model.AppEvent;
import ca.metricalsky.winston.events.model.EventSubscriptionEvent;
import ca.metricalsky.winston.mappers.ProblemMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@Getter
@Slf4j
public class SsePublisher {

    private final UUID id;
    private final SseEmitter sseEmitter;
    private boolean open;

    public SsePublisher(Long timeout) {
        this(new SseEmitter(timeout));
    }

    @VisibleForTesting
    SsePublisher(SseEmitter sseEmitter) {
        this.id = UUID.randomUUID();
        this.sseEmitter = sseEmitter;
        this.sseEmitter.onCompletion(() -> open = false);
        this.sseEmitter.onTimeout(() -> open = false);
        this.sseEmitter.onError(_ -> open = false);
        this.open = true;
    }

    public void complete() {
        publish(new EventSubscriptionEvent(id, false));
        sseEmitter.complete();
    }

    public void completeWithError(Throwable ex) {
        if (open) {
            var problem = new ProblemMapper().convert(ex);
            publish(new EventSubscriptionEvent(id, problem));
        }
        sseEmitter.completeWithError(ex);
    }

    public void publish(AppEvent appEvent) {
        publish(SseEmitter.event()
                .id(appEvent.getEventId().toString())
                .name(appEvent.getEventType())
                .data(appEvent, MediaType.APPLICATION_JSON));
    }

    protected void publish(SseEmitter.SseEventBuilder builder) {
        if (open) {
            try {
                sseEmitter.send(builder);
            } catch (IOException | RuntimeException ex) {
                throw new PublisherException(ex);
            }
        } else {
            throw new PublisherException("The event publisher is closed and cannot publish new events.");
        }
    }
}

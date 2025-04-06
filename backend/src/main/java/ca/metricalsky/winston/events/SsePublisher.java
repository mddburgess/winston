package ca.metricalsky.winston.events;

import com.google.common.annotations.VisibleForTesting;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
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
        sseEmitter.complete();
    }

    public void completeWithError(Throwable ex) {
        sseEmitter.completeWithError(ex);
    }

    public void publish(SubscriptionEvent subscriptionEvent) {
        publish(SseEmitter.event()
                .id(UUID.randomUUID().toString())
                .data(subscriptionEvent, MediaType.APPLICATION_JSON));
    }

    public void publish(FetchEvent fetchEvent) {
        publish(SseEmitter.event()
                .id(UUID.randomUUID().toString())
                .name(fetchEvent.type())
                .data(fetchEvent, MediaType.APPLICATION_JSON));
    }

    public void publish(ProblemDetail error) {
        publish(SseEmitter.event()
                .id(UUID.randomUUID().toString())
                .name("error")
                .data(error, MediaType.APPLICATION_PROBLEM_JSON));
    }

    private void publish(SseEmitter.SseEventBuilder builder) {
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

package ca.metricalsky.winston.events;

import ca.metricalsky.winston.events.model.EventSubscriptionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SsePublisherTest {

    private SsePublisher ssePublisher;

    @Mock
    private SseEmitter sseEmitter;

    @BeforeEach
    void beforeEach() {
        ssePublisher = new SsePublisher(sseEmitter);
    }

    @Test
    void complete() {
        ssePublisher.complete();

        verify(sseEmitter).complete();
    }

    @Test
    void completeWithError() {
        var exception = new RuntimeException();

        ssePublisher.completeWithError(exception);

        verify(sseEmitter).completeWithError(exception);
    }

    @Test
    void publish_ioException() throws Exception {
        var appEvent = new EventSubscriptionEvent(UUID.randomUUID(), true);
        doThrow(IOException.class)
                .when(sseEmitter).send(any(SseEmitter.SseEventBuilder.class));

        assertThatThrownBy(() -> ssePublisher.publish(appEvent))
                .isExactlyInstanceOf(PublisherException.class)
                .hasCauseExactlyInstanceOf(IOException.class);
    }
}

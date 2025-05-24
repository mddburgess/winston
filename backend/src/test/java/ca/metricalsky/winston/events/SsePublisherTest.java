package ca.metricalsky.winston.events;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.service.fetch.FetchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
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
    void publishSubscriptionEvent() throws Exception {
        var subscriptionEvent = new SubscriptionEvent(true, UUID.randomUUID());

        ssePublisher.publish(subscriptionEvent);

        verify(sseEmitter).send(any(SseEmitter.SseEventBuilder.class));
    }

    @Test
    void publishFetchDataEvent() throws Exception {
        var fetchResult = new FetchResult<>(new FetchActionEntity(), List.of(new Object()), null);
        var fetchDataEvent = FetchDataEvent.of(fetchResult);

        ssePublisher.publish(fetchDataEvent);

        verify(sseEmitter).send(any(SseEmitter.SseEventBuilder.class));
    }

    @Test
    void publishFetchStatusEvent() throws Exception {
        var fetchStatusEvent = FetchStatusEvent.completed();

        ssePublisher.publish(fetchStatusEvent);

        verify(sseEmitter).send(any(SseEmitter.SseEventBuilder.class));
    }

    @Test
    void publishProblemDetail() throws Exception {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        ssePublisher.publish(problemDetail);

        verify(sseEmitter).send(any(SseEmitter.SseEventBuilder.class));
    }

    @Test
    void publish_ioException() throws Exception {
        var fetchStatusEvent = FetchStatusEvent.completed();
        doThrow(IOException.class)
                .when(sseEmitter).send(any(SseEmitter.SseEventBuilder.class));

        assertThatThrownBy(() -> ssePublisher.publish(fetchStatusEvent))
                .isExactlyInstanceOf(PublisherException.class)
                .hasCauseExactlyInstanceOf(IOException.class);
    }
}

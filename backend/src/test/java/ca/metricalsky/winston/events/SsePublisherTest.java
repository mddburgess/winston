package ca.metricalsky.winston.events;

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
    void publishFetchEvent() throws Exception {
        var fetchEvent = FetchEvent.data("type", "objectId", FetchStatus.COMPLETED, List.of(new Object()));

        ssePublisher.publish(fetchEvent);

        verify(sseEmitter).send(any(SseEmitter.SseEventBuilder.class));
    }

    @Test
    void publishFetchEvent_ioException() throws Exception {
        var fetchEvent = FetchEvent.data("type", "objectId", FetchStatus.COMPLETED, List.of(new Object()));
        doThrow(IOException.class)
                .when(sseEmitter).send(any(SseEmitter.SseEventBuilder.class));

        assertThatThrownBy(() -> ssePublisher.publish(fetchEvent))
                .isExactlyInstanceOf(PublisherException.class)
                .hasCauseExactlyInstanceOf(IOException.class);
    }

    @Test
    void publishProblemDetail() throws Exception {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        ssePublisher.publish(problemDetail);

        verify(sseEmitter).send(any(SseEmitter.SseEventBuilder.class));
    }

    @Test
    void publishProblemDetail_ioException() throws Exception {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        doThrow(IOException.class)
                .when(sseEmitter).send(any(SseEmitter.SseEventBuilder.class));

        assertThatThrownBy(() -> ssePublisher.publish(problemDetail))
                .isExactlyInstanceOf(PublisherException.class)
                .hasCauseExactlyInstanceOf(IOException.class);
    }
}

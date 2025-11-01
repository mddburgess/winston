package ca.metricalsky.winston.events;

import ca.metricalsky.winston.api.model.Problem;
import ca.metricalsky.winston.events.model.AppEvent;
import ca.metricalsky.winston.test.annotations.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@UnitTest
class EventPublisherTest {

    @InjectMocks
    private EventPublisher eventPublisher;

    @Mock
    private ConversionService conversionService;
    @Mock
    private SsePublisher ssePublisher;

    private final Object object = new Object();
    private final AppEvent appEvent = mock(AppEvent.class);

    @BeforeEach
    void beforeEach() {
        doReturn(appEvent)
                .when(conversionService).convert(object, AppEvent.class);
    }

    @Test
    void publishEvent() {
        eventPublisher.publishEvent(object);

        assertThat(appEvent.getError())
                .isNull();
        verify(ssePublisher)
                .publish(appEvent);
    }

    @Test
    @Disabled
    void publishEvent_withException() {
        var exception = new RuntimeException();
        var problem = new Problem();

        doReturn(problem)
                .when(conversionService).convert(exception, Problem.class);

        eventPublisher.publishEvent(object, exception);

        assertThat(appEvent.getError())
                .isEqualTo(problem);
        verify(ssePublisher)
                .publish(appEvent);
    }
}

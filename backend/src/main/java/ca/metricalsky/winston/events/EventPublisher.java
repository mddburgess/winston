package ca.metricalsky.winston.events;

import ca.metricalsky.winston.config.exception.AppProblemDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final ConversionService conversionService;
    private final SsePublisher ssePublisher;

    public void publishEvent(Object object) {
        var appEvent = conversionService.convert(object, AppEvent.class);
        ssePublisher.publish(appEvent);
    }

    public void publishEvent(Object object, Throwable throwable) {
        var appEvent = conversionService.convert(object, AppEvent.class);
        appEvent.setError(AppProblemDetail.forException(throwable));
        ssePublisher.publish(appEvent);
    }
}

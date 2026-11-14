package ca.metricalsky.winston.events;

import ca.metricalsky.winston.events.model.AppEvent;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.exception.WinstonException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final ConversionService conversionService;
    private final SsePublisher ssePublisher;

    public void publishEvent(
            @NonNull Object object
    ) {
        publishEvent(object, null);
    }

    public void publishEvent(
            @NonNull Object object,
            Throwable throwable
    ) {
        var appEvent = conversionService.convert(object, AppEvent.class);
        if (throwable instanceof WinstonException ex) {
            appEvent.setError(ex.getProblem());
        } else if (throwable != null) {
            appEvent.setError(new WinstonException(ErrorCode.RUNTIME_EXCEPTION, throwable).getProblem());
        }
        ssePublisher.publish(appEvent);
    }
}

package ca.metricalsky.winston.convert.events;

import ca.metricalsky.winston.events.model.AppEvent;
import ca.metricalsky.winston.events.model.PullChannelsEvent;
import ca.metricalsky.winston.service.fetch.FetchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class FetchResultToAppEventConverter implements Converter<FetchResult, AppEvent> {

    private final ConversionService conversionService;

    @Override
    public AppEvent convert(FetchResult source) {
        Class<? extends AppEvent> eventClass = switch (source.actionType()) {
            case CHANNELS -> PullChannelsEvent.class;
            default -> throw new IllegalArgumentException(source.actionType() + " is not supported");
        };
        return conversionService.convert(source, eventClass);
    }
}

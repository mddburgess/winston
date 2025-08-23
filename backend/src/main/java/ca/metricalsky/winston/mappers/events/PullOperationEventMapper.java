package ca.metricalsky.winston.mappers.events;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.AppEvent;
import ca.metricalsky.winston.events.PullOperationEvent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class PullOperationEventMapper implements Converter<FetchOperationEntity, AppEvent> {

    @Override
    public AppEvent convert(FetchOperationEntity source) {
        var pullOperationEvent = new PullOperationEvent();
        pullOperationEvent.setOperation(source);

        return pullOperationEvent;
    }
}

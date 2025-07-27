package ca.metricalsky.winston.events;

import ca.metricalsky.winston.service.fetch.FetchResult;

public class FetchDataEventBuilder implements EventBuilder {

    @Override
    public Object buildEvent(FetchResult fetchResult) {
        return FetchDataEvent.of(fetchResult);
    }
}

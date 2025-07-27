package ca.metricalsky.winston.events;

import ca.metricalsky.winston.service.fetch.FetchResult;

public interface EventBuilder {

    public Object buildEvent(FetchResult fetchResult);
}

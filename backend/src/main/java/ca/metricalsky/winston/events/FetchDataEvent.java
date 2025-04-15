package ca.metricalsky.winston.events;

import ca.metricalsky.winston.service.fetch.FetchResult;

public record FetchDataEvent(
        String objectId,
        Iterable<?> items
) {

    public static FetchDataEvent of(FetchResult<?> fetchResult) {
        return new FetchDataEvent(fetchResult.objectId(), fetchResult.items());
    }
}

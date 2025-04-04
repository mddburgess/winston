package ca.metricalsky.winston.events;

public record FetchEvent(
        String type,
        String objectId,
        FetchStatus status,
        Iterable<?> items
) {

}

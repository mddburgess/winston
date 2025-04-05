package ca.metricalsky.winston.events;

import ca.metricalsky.winston.config.exception.AppProblemDetail;
import org.springframework.http.ProblemDetail;

public record FetchEvent(
        String type,
        String objectId,
        FetchStatus status,
        Iterable<?> items,
        ProblemDetail error
) {

    public static FetchEvent data(String type, String objectId, FetchStatus status, Iterable<?> items) {
        return new FetchEvent(type, objectId, status, items, null);
    }

    public static FetchEvent error(String type, String objectId, Throwable ex) {
        return new FetchEvent(type, objectId, FetchStatus.FAILED, null, AppProblemDetail.forException(ex));
    }
}

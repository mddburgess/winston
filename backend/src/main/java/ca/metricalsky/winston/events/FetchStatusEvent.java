package ca.metricalsky.winston.events;

import ca.metricalsky.winston.config.exception.AppProblemDetail;
import org.springframework.http.ProblemDetail;

public record FetchStatusEvent(
        FetchStatus status,
        ProblemDetail error
) {

    public static FetchStatusEvent completed() {
        return new FetchStatusEvent(FetchStatus.COMPLETED, null);
    }

    public static FetchStatusEvent failed(Throwable ex) {
        return new FetchStatusEvent(FetchStatus.FAILED, AppProblemDetail.forException(ex));
    }
}

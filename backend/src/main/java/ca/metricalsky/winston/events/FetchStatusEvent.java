package ca.metricalsky.winston.events;

import ca.metricalsky.winston.api.model.PullOperation;
import ca.metricalsky.winston.config.exception.AppProblemDetail;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import org.springframework.http.ProblemDetail;

public record FetchStatusEvent(
        FetchOperationEntity operation,
        FetchStatus status,
        ProblemDetail error
) {

    public static FetchStatusEvent operation(FetchOperationEntity operation) {
        return new FetchStatusEvent(operation, null, null);
    }

    public static FetchStatusEvent completed() {
        return new FetchStatusEvent(null, FetchStatus.COMPLETED, null);
    }

    public static FetchStatusEvent failed(Throwable ex) {
        return new FetchStatusEvent(null, FetchStatus.FAILED, AppProblemDetail.forException(ex));
    }
}

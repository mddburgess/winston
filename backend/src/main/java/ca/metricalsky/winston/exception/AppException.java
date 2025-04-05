package ca.metricalsky.winston.exception;

import ca.metricalsky.winston.config.exception.AppProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponseException;

public class AppException extends ErrorResponseException {

    public AppException(HttpStatus status, String reason) {
        this(status, reason, null);
    }

    public AppException(Throwable cause) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, cause.getMessage(), cause);
    }

    protected AppException(HttpStatus status, String reason, Throwable cause) {
        super(status, AppProblemDetail.forException(cause), cause);
        getBody().setStatus(status);
        setDetail(reason);
    }

    @Override
    @NonNull
    public String getMessage() {
        return getStatusCode() + (getBody().getDetail() != null ? ": " + getBody().getDetail() : "");
    }
}

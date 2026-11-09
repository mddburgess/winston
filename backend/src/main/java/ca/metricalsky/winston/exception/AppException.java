package ca.metricalsky.winston.exception;

import ca.metricalsky.winston.config.exception.AppProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponseException;

public class AppException extends ErrorResponseException {

    public AppException(@NonNull ErrorCode errorCode, Throwable cause) {
        super(errorCode.getStatus(), errorCode.getProblemDetail(), cause);
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

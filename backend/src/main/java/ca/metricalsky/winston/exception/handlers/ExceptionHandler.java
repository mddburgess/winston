package ca.metricalsky.winston.exception.handlers;

import org.springframework.http.ProblemDetail;

public interface ExceptionHandler<T extends Throwable> {

    ProblemDetail handleException(T exception);
}

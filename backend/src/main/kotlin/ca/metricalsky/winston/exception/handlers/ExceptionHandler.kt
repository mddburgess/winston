package ca.metricalsky.winston.exception.handlers;

import ca.metricalsky.winston.api.model.Problem;

public interface ExceptionHandler<T extends Throwable> {

    Problem handleException(T exception);
}

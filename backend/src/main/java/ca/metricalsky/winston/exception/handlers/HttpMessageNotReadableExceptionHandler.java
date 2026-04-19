package ca.metricalsky.winston.exception.handlers;

import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.exception.Location;
import ca.metricalsky.winston.exception.ProblemError;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;

@Component
public class HttpMessageNotReadableExceptionHandler
        implements ExceptionHandler<HttpMessageNotReadableException> {

    @Override
    public ProblemDetail handleException(HttpMessageNotReadableException exception) {
        return switch (getRootCause(exception)) {
            case JsonParseException ex -> handleException(ex);
            case Throwable ex -> handleThrowable(ex);
        };
    }

    private ProblemDetail handleException(JsonParseException exception) {
        var detail = exception.getOriginalMessage()
                .replaceFirst("Source:.*; ", "");
        var location = new Location(exception.getLocation());
        var error = new ProblemError("error:malformed-json", detail, location);

        var problem = ErrorCode.MALFORMED_REQUEST_BODY.getProblemDetail();
        problem.setProperties(Map.of("errors", List.of(error)));
        return problem;
    }

    private ProblemDetail handleThrowable(Throwable ex) {
        var type = "error:class:"  + ex.getClass().getSimpleName();
        var detail = ex.getMessage();
        var error = new ProblemError(type, detail);

        var problem = ErrorCode.MALFORMED_REQUEST_BODY.getProblemDetail();
        problem.setProperties(Map.of("errors", List.of(error)));
        return problem;
    }
}

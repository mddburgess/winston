package ca.metricalsky.winston.exception.handlers;

import ca.metricalsky.winston.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
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
            default -> ErrorCode.MALFORMED_REQUEST_BODY.getProblemDetail();
        };
    }

    private ProblemDetail handleException(JsonParseException exception) {
        var message = exception.getOriginalMessage()
                .replaceFirst("Source:.*; ", "");

        var error = new LinkedHashMap<String, Object>();
        error.put("line", exception.getLocation().getLineNr());
        error.put("column", exception.getLocation().getColumnNr());
        error.put("detail", message);

        var problem = ErrorCode.MALFORMED_REQUEST_BODY.getProblemDetail();
        problem.setProperties(Map.of("errors", List.of(error)));
        return problem;
    }
}

package ca.metricalsky.winston.exception.handlers;

import ca.metricalsky.winston.api.model.Problem;
import ca.metricalsky.winston.api.model.ProblemError;
import ca.metricalsky.winston.api.model.ProblemLocation;
import ca.metricalsky.winston.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;

@Component
public class HttpMessageNotReadableExceptionHandler
        implements ExceptionHandler<HttpMessageNotReadableException> {

    @Override
    public Problem handleException(HttpMessageNotReadableException exception) {
        return switch (getRootCause(exception)) {
            case JsonParseException ex -> handleException(ex);
            case MismatchedInputException ex -> handleException(ex);
            case Throwable ex -> handleThrowable(ex);
        };
    }

    private Problem handleException(JsonParseException exception) {
        var detail = exception.getOriginalMessage()
                .replaceFirst("Source:.*; ", "");
        var location = exception.getLocation();
        var error = new ProblemError()
                .type("error:malformed-json")
                .detail(detail)
                .location(new ProblemLocation()
                        .line(location.getLineNr())
                        .column(location.getColumnNr()));
        return ErrorCode.MALFORMED_REQUEST_BODY.getProblem().addErrorsItem(error);
    }

    private Problem handleException(MismatchedInputException exception) {
        var error = new ProblemError()
                .type("error:invalid-type")
                .detail(exception.getMessage());
        return ErrorCode.MALFORMED_REQUEST_BODY.getProblem().addErrorsItem(error);
    }

    private Problem handleThrowable(Throwable ex) {
        if (ex.getMessage().startsWith("Required request body is missing: ")) {
            var error = new ProblemError()
                    .type("error:missing-request-body")
                    .detail("The request body must not be empty.");
            return ErrorCode.MALFORMED_REQUEST_BODY.getProblem().addErrorsItem(error);
        }

        var error = new ProblemError()
                .type("error:class:"  + ex.getClass().getSimpleName())
                .detail(ex.getMessage());
        return ErrorCode.MALFORMED_REQUEST_BODY.getProblem().addErrorsItem(error);
    }
}

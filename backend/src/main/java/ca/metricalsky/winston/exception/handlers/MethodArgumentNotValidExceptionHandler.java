package ca.metricalsky.winston.exception.handlers;

import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.exception.Location;
import ca.metricalsky.winston.exception.ProblemError;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Locale;
import java.util.Map;

import static ca.metricalsky.winston.exception.utils.JsonExceptionUtils.getJsonPointer;

@Component
@RequiredArgsConstructor
public class MethodArgumentNotValidExceptionHandler
        implements ExceptionHandler<MethodArgumentNotValidException> {

    private final MessageSource messageSource;

    @Override
    public ProblemDetail handleException(MethodArgumentNotValidException exception) {

        var errors = exception.getFieldErrors().stream()
                .map(this::buildError)
                .toList();

        var problem = ErrorCode.INVALID_REQUEST_BODY.getProblemDetail();
        problem.setProperties(Map.of("errors", errors));
        return problem;
    }

    private ProblemError buildError(FieldError fieldError) {
        var type = "error:" + fieldError.getCode();
        var detail = messageSource.getMessage(fieldError, Locale.ENGLISH);
        var location = new Location(getJsonPointer(fieldError));

        return new ProblemError(type, detail, location);
    }
}

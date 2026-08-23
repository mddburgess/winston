package ca.metricalsky.winston.exception.handlers;

import ca.metricalsky.winston.api.model.Problem;
import ca.metricalsky.winston.api.model.ProblemError;
import ca.metricalsky.winston.api.model.ProblemLocation;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.exception.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Locale;

import static ca.metricalsky.winston.exception.utils.JsonExceptionUtils.getJsonPointer;

@Component
@RequiredArgsConstructor
public class MethodArgumentNotValidExceptionHandler
        implements ExceptionHandler<MethodArgumentNotValidException> {

    private final MessageSource messageSource;

    @Override
    public Problem handleException(MethodArgumentNotValidException exception) {

        var errors = exception.getFieldErrors().stream()
                .map(this::buildError)
                .toList();
        return ErrorCode.INVALID_REQUEST_BODY.getProblem().errors(errors);
    }

    private ProblemError buildError(FieldError fieldError) {
        var type = "error:" + fieldError.getCode();
        var detail = messageSource.getMessage(fieldError, Locale.ENGLISH);
        var location = new Location(getJsonPointer(fieldError));

        return new ProblemError()
                .type(type)
                .detail(detail)
                .location(new ProblemLocation().line(location.line()).column(location.column()));
    }
}

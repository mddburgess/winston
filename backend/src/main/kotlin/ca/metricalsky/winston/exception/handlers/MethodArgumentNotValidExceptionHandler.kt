package ca.metricalsky.winston.exception.handlers

import ca.metricalsky.winston.api.model.Problem
import ca.metricalsky.winston.api.model.ProblemError
import ca.metricalsky.winston.api.model.ProblemLocation
import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.Location
import ca.metricalsky.winston.exception.utils.JsonExceptionUtils
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import java.util.Locale

@Component
class MethodArgumentNotValidExceptionHandler(
    private val messageSource: MessageSource
): ExceptionHandler<MethodArgumentNotValidException> {

    override fun handleException(exception: MethodArgumentNotValidException): Problem {
        val errors = exception.fieldErrors.map { buildError(it) }
        return ErrorCode.INVALID_REQUEST_BODY.problem.errors(errors)
    }

    private fun buildError(fieldError: FieldError): ProblemError {
        val location = Location(JsonExceptionUtils.getJsonPointer(fieldError))

        return ProblemError()
            .type("error:${fieldError.code}")
            .detail(messageSource.getMessage(fieldError, Locale.ENGLISH))
            .location(ProblemLocation()
                .pointer(location.pointer)
                .line(location.line)
                .column(location.column))
    }
}

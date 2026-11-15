package ca.metricalsky.winston.exception.handlers

import ca.metricalsky.winston.api.model.Problem
import ca.metricalsky.winston.api.model.ProblemError
import ca.metricalsky.winston.api.model.ProblemLocation
import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.utils.jsonPointer
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import java.util.*

@Component
class MethodArgumentNotValidExceptionHandler(
    private val messageSource: MessageSource
) : ExceptionHandler<MethodArgumentNotValidException> {

    override fun handleException(exception: MethodArgumentNotValidException): Problem =
        ErrorCode.INVALID_REQUEST_BODY.problem.errors(exception.fieldErrors.map {
            buildError(it)
        })

    private fun buildError(fieldError: FieldError): ProblemError = ProblemError()
        .type("error:${fieldError.code}")
        .detail(messageSource.getMessage(fieldError, Locale.ENGLISH))
        .location(ProblemLocation().pointer(fieldError.jsonPointer.toString()))
}

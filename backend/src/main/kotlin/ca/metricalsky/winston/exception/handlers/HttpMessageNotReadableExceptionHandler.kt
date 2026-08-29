package ca.metricalsky.winston.exception.handlers

import ca.metricalsky.winston.api.model.Problem
import ca.metricalsky.winston.api.model.ProblemError
import ca.metricalsky.winston.api.model.ProblemLocation
import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.utils.JsonExceptionUtils
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component

@Component
class HttpMessageNotReadableExceptionHandler: ExceptionHandler<HttpMessageNotReadableException> {

    override fun handleException(exception: HttpMessageNotReadableException) =
        when (val cause = exception.cause) {
            null -> handleSelf(exception)
            else -> handleExceptionInternal(cause)
        }

    private fun handleExceptionInternal(exception: Throwable): Problem =
        when (exception) {
            is JsonParseException -> handleException(exception)
            is MismatchedInputException -> handleException(exception)
            is ValueInstantiationException -> handleException(exception)
            else -> when (val cause = exception.cause) {
                null -> handleThrowable(exception)
                else -> handleExceptionInternal(cause)
            }
        }


    private fun handleException(exception: JsonParseException): Problem {
        val detail = exception.originalMessage.replaceFirst(Regex("Source:.*; "), "")
        val error = ProblemError()
            .type("error:malformed-json")
            .detail(detail)
            .location(
                ProblemLocation()
                    .line(exception.location.lineNr)
                    .column(exception.location.columnNr)
            )
        return ErrorCode.MALFORMED_REQUEST_BODY.problem.addErrorsItem(error)
    }

    private fun handleException(exception: MismatchedInputException): Problem {
        val error = ProblemError()
            .type("error:invalid-property-type")
            .detail("The value type for a property in the request is invalid.")
            .location(
                ProblemLocation()
                    .pointer(JsonExceptionUtils.getJsonPointer(exception).toString())
                    .line(exception.location.lineNr)
                    .column(exception.location.columnNr)
            )
            .expected(JsonExceptionUtils.getJsonType(exception.targetType))
        return ErrorCode.MALFORMED_REQUEST_BODY.problem.addErrorsItem(error)
    }

    private fun handleException(exception: ValueInstantiationException): Problem {
        val error = ProblemError()
            .type("error:invalid-property-value")
            .detail("The value for a property in the request is invalid.")
            .location(
                ProblemLocation()
                    .pointer(JsonExceptionUtils.getJsonPointer(exception).toString())
                    .line(exception.location.lineNr)
                    .column(exception.location.columnNr)
            )
        return ErrorCode.MALFORMED_REQUEST_BODY.problem.addErrorsItem(error)
    }

    private fun handleSelf(exception: HttpMessageNotReadableException): Problem {
        if (exception.message?.startsWith("Required request body is missing: ") == true) {
            val error = ProblemError()
                .type("error:missing-request-body")
                .detail("The request body must not be empty.")
            return ErrorCode.MALFORMED_REQUEST_BODY.problem.addErrorsItem(error)
        }
        return handleThrowable(exception)
    }

    private fun handleThrowable(ex: Throwable): Problem {
        val error = ProblemError()
            .type("error:class:" + ex.javaClass.simpleName)
            .detail(ex.message)
        return ErrorCode.MALFORMED_REQUEST_BODY.problem.addErrorsItem(error)
    }
}

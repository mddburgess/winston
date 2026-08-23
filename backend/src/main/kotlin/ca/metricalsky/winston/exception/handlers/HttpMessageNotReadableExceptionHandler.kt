package ca.metricalsky.winston.exception.handlers

import ca.metricalsky.winston.api.model.Problem
import ca.metricalsky.winston.api.model.ProblemError
import ca.metricalsky.winston.api.model.ProblemLocation
import ca.metricalsky.winston.exception.ErrorCode
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component

@Component
class HttpMessageNotReadableExceptionHandler: ExceptionHandler<HttpMessageNotReadableException> {

    override fun handleException(exception: HttpMessageNotReadableException) =
        when (val rootCause = ExceptionUtils.getRootCause(exception)) {
            is JsonParseException -> handleException(rootCause)
            is MismatchedInputException -> handleException(rootCause)
            else -> handleThrowable(rootCause)
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
            .type("error:invalid-type")
            .detail(exception.message)
        return ErrorCode.MALFORMED_REQUEST_BODY.problem.addErrorsItem(error)
    }

    private fun handleThrowable(ex: Throwable): Problem {
        if (ex.message?.startsWith("Required request body is missing: ") == true) {
            val error = ProblemError()
                .type("error:missing-request-body")
                .detail("The request body must not be empty.")
            return ErrorCode.MALFORMED_REQUEST_BODY.problem.addErrorsItem(error)
        }

        val error = ProblemError()
            .type("error:class:" + ex.javaClass.simpleName)
            .detail(ex.message)
        return ErrorCode.MALFORMED_REQUEST_BODY.problem.addErrorsItem(error)
    }
}

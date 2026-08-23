package ca.metricalsky.winston.exception.handlers

import ca.metricalsky.winston.api.model.Problem
import ca.metricalsky.winston.api.model.ProblemError
import ca.metricalsky.winston.api.model.ProblemLocation
import ca.metricalsky.winston.exception.ErrorCode
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.stereotype.Component

@Component
class ConstraintViolationExceptionHandler: ExceptionHandler<ConstraintViolationException> {

    override fun handleException(exception: ConstraintViolationException): Problem {
        val errors = exception.constraintViolations.map { buildError(it) }
        return ErrorCode.INVALID_REQUEST_BODY.problem.errors(errors)
    }

    private fun buildError(violation: ConstraintViolation<*>): ProblemError {
        return ProblemError()
            .type("error:${violation.messageTemplate}")
            .detail(violation.message)
            .location(ProblemLocation().pointer(violation.propertyPath.toString()))
    }
}

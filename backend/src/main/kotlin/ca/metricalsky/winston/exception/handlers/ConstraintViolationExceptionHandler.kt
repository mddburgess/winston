package ca.metricalsky.winston.exception.handlers

import ca.metricalsky.winston.api.model.Problem
import ca.metricalsky.winston.api.model.ProblemError
import ca.metricalsky.winston.api.model.ProblemLocation
import ca.metricalsky.winston.exception.ErrorCode
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import jakarta.validation.ElementKind
import jakarta.validation.Path
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class ConstraintViolationExceptionHandler(
    private val messageSource: MessageSource
): ExceptionHandler<ConstraintViolationException> {

    private val types = mapOf(
        NotNull::class.java to "missing-required-property",
        Size::class.java to "invalid-property-length",
    )

    override fun handleException(exception: ConstraintViolationException): Problem {
        val errors = exception.constraintViolations.map { buildError(it) }
        return ErrorCode.INVALID_REQUEST_BODY.problem.errors(errors)
    }

    private fun buildError(violation: ConstraintViolation<*>): ProblemError {
        val type = when (val descriptor = violation.constraintDescriptor) {
            is ConstraintDescriptorImpl -> types[descriptor.annotationType]
                ?: "constraint:${descriptor.annotationType.simpleName}"
            else -> violation.constraintDescriptor.messageTemplate
        }
        val message = try {
            messageSource.getMessage(type, null, Locale.ENGLISH)
        } catch (e: Exception) {
            violation.message
        }

        return ProblemError()
            .type("error:${type}")
            .detail(message)
            .location(ProblemLocation().pointer(violation.propertyPath.toJsonPointer()))
    }

    private fun Path.toJsonPointer() =
        this.filter { it.kind == ElementKind.PROPERTY }
            .flatMap { listOf(it.index, it.name) }
            .filterNotNull()
            .joinToString(prefix = "/", separator = "/") { it.toString() }
}

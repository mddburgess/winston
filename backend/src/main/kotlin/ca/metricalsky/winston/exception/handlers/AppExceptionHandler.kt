package ca.metricalsky.winston.exception.handlers

import ca.metricalsky.winston.exception.AppException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class AppExceptionHandler(
    private val constraintViolationHandler: ConstraintViolationExceptionHandler,
    private val httpMessageNotReadableHandler: HttpMessageNotReadableExceptionHandler,
    private val methodArgumentNotValidHandler: MethodArgumentNotValidExceptionHandler,
) : ResponseEntityExceptionHandler() {

    private val log = KotlinLogging.logger {}

    @ExceptionHandler(AppException::class)
    fun handleAppException(ex: AppException) = ex.also {
        log.error(ex) { "Request has thrown an exception of type ${ex.javaClass.simpleName}" }
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException
    ): ResponseEntity<Any?>? {
        var body = constraintViolationHandler.handleException(ex)
        return handleExceptionInternal(ex, body, null, HttpStatus.BAD_REQUEST, null)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException) = AppException(ex).also {
        log.error(ex) { "Request has thrown an exception of type ${ex.javaClass.simpleName}" }
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any?>? {
        log.error(ex) { "Request has thrown an exception of type ${ex.javaClass.simpleName}" }
        val body = httpMessageNotReadableHandler.handleException(ex)
        return handleExceptionInternal(ex, body, headers, status, request)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any?>? {
        log.error(ex) { "Request has thrown an exception of type ${ex.javaClass.simpleName}" }
        val body = methodArgumentNotValidHandler.handleException(ex)
        return handleExceptionInternal(ex, body, headers, status, request)
    }
}

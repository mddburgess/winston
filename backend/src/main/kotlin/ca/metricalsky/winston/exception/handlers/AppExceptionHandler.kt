package ca.metricalsky.winston.exception.handlers

import ca.metricalsky.winston.api.ext.ProblemException
import ca.metricalsky.winston.api.model.Problem
import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.WinstonException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.ConstraintViolationException
import org.slf4j.MDC
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

private val REQUEST_ID_HEADER = "X-Request-Id";

@RestControllerAdvice
class AppExceptionHandler(
    private val constraintViolationHandler: ConstraintViolationExceptionHandler,
    private val httpMessageNotReadableHandler: HttpMessageNotReadableExceptionHandler,
    private val methodArgumentNotValidHandler: MethodArgumentNotValidExceptionHandler,
) : ResponseEntityExceptionHandler() {

    private val log = KotlinLogging.logger {}

    @ExceptionHandler(WinstonException::class)
    fun handleWinstonException(
        ex: WinstonException,
        request: WebRequest,
    ): ResponseEntity<Any?>? {
        log.error(ex) { "Request has thrown an exception of type ${ex.javaClass.simpleName}" }
        return handleExceptionInternal(ex, ex.problem, HttpHeaders.EMPTY, ex.status, request)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: WebRequest,
    ): ResponseEntity<Any?>? {
        val body = constraintViolationHandler.handleException(ex)
        return handleExceptionInternal(ex, body, HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(
        ex: RuntimeException,
        request: WebRequest,
    ): ResponseEntity<Any?>? {
        log.error(ex) { "Request has thrown an exception of type ${ex.javaClass.simpleName}" }
        val exception = WinstonException(ErrorCode.RUNTIME_EXCEPTION, ex).also {
            it.problem.exception = ProblemException(ex)
        }
        return handleExceptionInternal(exception, exception.problem, HttpHeaders.EMPTY, exception.status, request)
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

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any?>? {
        if (body is Problem) {
            try {
                val requestId = MDC.get(REQUEST_ID_HEADER)
                if (requestId != null) {
                    body.instance = "api:request:${requestId}"
                }
            } catch (_: Exception) {
            }
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request)
    }
}

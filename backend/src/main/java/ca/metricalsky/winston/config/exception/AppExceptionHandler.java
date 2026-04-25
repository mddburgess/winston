package ca.metricalsky.winston.config.exception;

import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.handlers.HttpMessageNotReadableExceptionHandler;
import ca.metricalsky.winston.exception.handlers.MethodArgumentNotValidExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    private final HttpMessageNotReadableExceptionHandler httpMessageNotReadableHandler;
    private final MethodArgumentNotValidExceptionHandler methodArgumentNotValidHandler;

    @ExceptionHandler(AppException.class)
    public ErrorResponse handleAppException(AppException ex) {
        log.error("Request has thrown an exception of type {}", ex.getClass().getSimpleName(), ex);
        return ex;
    }

    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        log.error("Request has thrown an exception of type {}", ex.getClass().getSimpleName(), ex);
        return new AppException(ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        var body = httpMessageNotReadableHandler.handleException(ex);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        var body = methodArgumentNotValidHandler.handleException(ex);
        return handleExceptionInternal(ex, body, headers, status, request);
    }
}

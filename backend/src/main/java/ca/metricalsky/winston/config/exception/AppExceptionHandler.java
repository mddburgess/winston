package ca.metricalsky.winston.config.exception;

import ca.metricalsky.winston.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

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
}

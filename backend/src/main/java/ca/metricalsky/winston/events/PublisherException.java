package ca.metricalsky.winston.events;

import ca.metricalsky.winston.exception.AppException;
import org.springframework.http.HttpStatus;

public class PublisherException extends AppException {

    public PublisherException(String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason, null);
    }

    public PublisherException(Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, cause.getMessage(), cause);
    }
}

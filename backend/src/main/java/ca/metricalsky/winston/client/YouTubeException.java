package ca.metricalsky.winston.client;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public class YouTubeException extends ResponseStatusException {

    protected YouTubeException(HttpStatus status, String message, Throwable cause) {
        super(status, message, cause);
    }

    private YouTubeException(GoogleJsonResponseException cause) {
        super(cause.getStatusCode(), cause.getDetails().getMessage(), cause);
    }

    private YouTubeException(Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, cause.getMessage(), cause);
    }

    public static YouTubeException wrap(Throwable cause) {
        if (cause instanceof GoogleJsonResponseException googleException) {
            var reason = Optional.of(googleException)
                        .map(GoogleJsonResponseException::getDetails)
                        .map(GoogleJsonError::getErrors)
                        .map(List::getFirst)
                        .map(GoogleJsonError.ErrorInfo::getReason)
                        .orElse("");

            return switch (reason) {
                case "commentsDisabled" -> new CommentsDisabledException(googleException);
                default -> new YouTubeException(googleException);
            };
        }
        return new YouTubeException(cause);
    }
}

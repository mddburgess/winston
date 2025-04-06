package ca.metricalsky.winston.client;

import ca.metricalsky.winston.exception.AppException;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public class YouTubeException extends AppException {

    protected YouTubeException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    private YouTubeException(GoogleJsonResponseException cause) {
        super(HttpStatus.valueOf(cause.getStatusCode()), cause.getDetails().getMessage(), cause);
    }

    private YouTubeException(Throwable cause) {
        super(cause);
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

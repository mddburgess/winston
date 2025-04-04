package ca.metricalsky.winston.client;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class YouTubeException extends ResponseStatusException {

    private YouTubeException(GoogleJsonResponseException cause) {
        super(cause.getStatusCode(), cause.getDetails().getMessage(), cause);
    }

    private YouTubeException(Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, cause.getMessage(), cause);
    }

    public static YouTubeException wrap(Throwable cause) {
        if (cause instanceof GoogleJsonResponseException googleException) {
            return new YouTubeException(googleException);
        }
        return new YouTubeException(cause);
    }
}

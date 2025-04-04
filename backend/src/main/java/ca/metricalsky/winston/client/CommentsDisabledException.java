package ca.metricalsky.winston.client;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.springframework.http.HttpStatus;

public class CommentsDisabledException extends YouTubeException {

    private static final String MESSAGE = "Comments are disabled for the requested video.";

    public CommentsDisabledException(GoogleJsonResponseException cause) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, MESSAGE, cause);
    }
}

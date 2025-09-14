package ca.metricalsky.winston.client;

import ca.metricalsky.winston.exception.ErrorCode;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class CommentsDisabledException extends YouTubeException {

    public CommentsDisabledException(GoogleJsonResponseException cause) {
        super(ErrorCode.COMMENTS_DISABLED, cause);
    }
}

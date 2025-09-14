package ca.metricalsky.winston.client;

import ca.metricalsky.winston.exception.ErrorCode;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class VideoNotFoundException extends YouTubeException {

    public VideoNotFoundException(GoogleJsonResponseException cause) {
        super(ErrorCode.VIDEO_NOT_FOUND, cause);
    }
}

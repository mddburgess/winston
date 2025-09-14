package ca.metricalsky.winston.client;

import ca.metricalsky.winston.exception.ErrorCode;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class QuotaExceededException extends YouTubeException {

    public QuotaExceededException(GoogleJsonResponseException cause) {
        super(ErrorCode.QUOTA_EXCEEDED, cause);
    }
}

package ca.metricalsky.winston.client;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.springframework.http.HttpStatus;

import java.net.URI;

public class QuotaExceededException extends YouTubeException {

    private static final URI TYPE = URI.create("/api/problem/quota-exceeded");
    private static final String MESSAGE = "The YouTube API request quota has been exceeded.";

    public QuotaExceededException(GoogleJsonResponseException cause) {
        super(HttpStatus.TOO_MANY_REQUESTS, MESSAGE, cause);
        getBody().setType(TYPE);
    }
}

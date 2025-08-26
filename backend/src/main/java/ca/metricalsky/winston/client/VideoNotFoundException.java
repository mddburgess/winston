package ca.metricalsky.winston.client;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.springframework.http.HttpStatus;

import java.net.URI;

public class VideoNotFoundException extends YouTubeException {

    private static final URI TYPE = URI.create("/api/problem/video-not-found");
    private static final String MESSAGE = "The requested video was not found.";

    public VideoNotFoundException(GoogleJsonResponseException cause) {
        super(HttpStatus.NOT_FOUND, MESSAGE, cause);
        getBody().setType(TYPE);
    }
}

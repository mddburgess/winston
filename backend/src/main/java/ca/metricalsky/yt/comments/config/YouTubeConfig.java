package ca.metricalsky.yt.comments.config;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YouTubeConfig {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final GsonFactory JSON_FACTORY = new GsonFactory();
    private static final HttpRequestInitializer HTTP_REQUEST_INITIALIZER = _ -> {};

    @Value("${youtube.apiKey}")
    private String apiKey;

    @Value("${youtube.mock.rootUrl:}")
    private String mockRootUrl;

    @Bean
    public YouTube youTube() {
        var youTubeBuilder = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, HTTP_REQUEST_INITIALIZER)
                .setYouTubeRequestInitializer(new YouTubeRequestInitializer(apiKey));

        if (!mockRootUrl.isBlank()) {
            youTubeBuilder.setRootUrl(mockRootUrl);
        };

        return youTubeBuilder.build();
    }
}

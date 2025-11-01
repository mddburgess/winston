package ca.metricalsky.winston.config;

import ca.metricalsky.winston.config.properties.youtube.YouTubeConfig;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class YouTubeProvider {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final GsonFactory JSON_FACTORY = new GsonFactory();
    private static final HttpRequestInitializer HTTP_REQUEST_INITIALIZER = _ -> {};

    private final YouTubeConfig youTubeConfig;

    @Bean
    public YouTube youTube() {
        var youTubeBuilder = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, HTTP_REQUEST_INITIALIZER)
                .setYouTubeRequestInitializer(new YouTubeRequestInitializer(youTubeConfig.getApiKey()));

        if (StringUtils.isNotBlank(youTubeConfig.getMockRootUrl())) {
            youTubeBuilder.setRootUrl(youTubeConfig.getMockRootUrl());
        };

        return youTubeBuilder.build();
    }
}

package ca.metricalsky.winston.config.properties.youtube;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "winston.youtube")
@Value
public class YouTubeConfig {

    /**
     * API key for YouTube API requests.
     */
    @NotBlank
    String apiKey;

    /**
     * Daily request quota available for YouTube API requests.
     */
    @NotNull
    @Min(0)
    @Max(10_000)
    Integer dailyRequestQuota;

    /**
     * Root URL for a mock YouTube API service. Use for testing only.
     */
    String mockRootUrl;
}

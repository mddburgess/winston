package ca.metricalsky.winston.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "api.videos")
@Value
public class VideosApiConfig {

    /**
     * Default page size for the list videos endpoint.
     */
    @NotNull
    @Min(1)
    @Max(500)
    Integer defaultPageSize;
}

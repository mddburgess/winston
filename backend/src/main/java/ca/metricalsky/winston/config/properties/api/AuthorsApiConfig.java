package ca.metricalsky.winston.config.properties.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "winston.api.authors")
@Value
public class AuthorsApiConfig {

    /**
     * Default page size for the list authors endpoint.
     */
    @NotNull
    @Min(1)
    @Max(500)
    Integer defaultPageSize;
}

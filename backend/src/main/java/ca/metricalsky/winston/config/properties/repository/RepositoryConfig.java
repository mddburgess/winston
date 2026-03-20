package ca.metricalsky.winston.config.properties.repository;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "winston.repository")
@Value
public class RepositoryConfig {

    /**
     * Default service level objective (in milliseconds) for repository methods.
     */
    @NotNull
    @Min(0)
    Integer defaultSloMs;
}

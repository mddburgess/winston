package ca.metricalsky.winston.config.properties.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "winston.controller")
@Value
public class ControllerConfig {

    /**
     * Default service level objective (in milliseconds) for controller methods.
     */
    @NotNull
    @Min(0)
    Integer defaultSloMs;
}

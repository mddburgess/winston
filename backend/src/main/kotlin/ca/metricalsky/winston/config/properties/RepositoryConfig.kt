package ca.metricalsky.winston.config.properties

import jakarta.validation.constraints.Min
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "winston.repository")
class RepositoryConfig (

    /**
     * Default service level objective (in milliseconds) for repository methods.
     */
    @field:Min(0)
    var defaultSloMs: Int,
)

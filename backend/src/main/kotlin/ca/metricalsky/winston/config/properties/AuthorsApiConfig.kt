package ca.metricalsky.winston.config.properties

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "winston.api.authors")
class AuthorsApiConfig(

    /**
     * Default page size for the list authors endpoint.
     */
    @field:Min(1)
    @field:Max(500)
    var defaultPageSize: Int,
)

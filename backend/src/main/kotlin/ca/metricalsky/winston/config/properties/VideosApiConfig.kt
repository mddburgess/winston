package ca.metricalsky.winston.config.properties

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import lombok.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "winston.api.videos")
class VideosApiConfig(

    /**
     * Default page size for the list videos endpoint.
     */
    @field:Min(1)
    @field:Max(500)
    var defaultPageSize: Int,
)

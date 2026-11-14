package ca.metricalsky.winston.config.properties

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "winston.youtube")
data class YouTubeConfig(

    /**
     * API key for YouTube API requests.
     */
    @field:NotBlank
    val apiKey: String,

    /**
     * Daily request quota available for YouTube API requests.
     */
    @field:Min(0)
    @field:Max(10_000)
    val dailyRequestQuota: Int,

    /**
     * Root URL for a mock YouTube API service. Use for testing only.
     */
    val mockRootUrl: String? = null,
)

package ca.metricalsky.winston.config

import ca.metricalsky.winston.config.properties.YouTubeConfig
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeRequestInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class YouTubeProvider(
    private val youTubeConfig: YouTubeConfig,
) {
    private val httpRequestInitializer = HttpRequestInitializer {}
    private val httpTransport = NetHttpTransport()
    private val jsonFactory = GsonFactory()

    @Bean
    fun youTube(): YouTube = YouTube.Builder(httpTransport, jsonFactory, httpRequestInitializer)
        .setYouTubeRequestInitializer(YouTubeRequestInitializer(youTubeConfig.apiKey))
        .apply {
            if (youTubeConfig.mockRootUrl.isNotBlank()) {
                setRootUrl(youTubeConfig.mockRootUrl)
            }
        }.build()
}

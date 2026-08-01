package ca.metricalsky.winston.test.faker.providers.youtube

import ca.metricalsky.winston.test.faker.WinstonFaker
import com.google.api.client.util.DateTime
import com.google.api.services.youtube.model.Channel
import com.google.api.services.youtube.model.ChannelBrandingSettings
import com.google.api.services.youtube.model.ChannelContentDetails
import com.google.api.services.youtube.model.ChannelContentDetails.RelatedPlaylists
import com.google.api.services.youtube.model.ChannelSettings
import com.google.api.services.youtube.model.ChannelSnippet
import com.google.api.services.youtube.model.ChannelStatistics
import com.google.api.services.youtube.model.Thumbnail
import com.google.api.services.youtube.model.ThumbnailDetails
import net.datafaker.providers.base.AbstractProvider

class ChannelProvider(faker: WinstonFaker) : AbstractProvider<WinstonFaker>(faker) {

    fun id() = faker.regexify("UC[A-Za-z0-9_-]{22}")

    fun handle() = "@" + faker.word().noun()

    fun minimalObject() = Channel().apply {
        id = id()
    }

    fun completeObject() = Channel().apply {
        id = id()
        snippet = ChannelSnippet().apply {
            title = faker.massEffect().character()
            description = faker.massEffect().quote()
            customUrl = handle()
            publishedAt = DateTime.parseRfc3339("2025-01-01T00:00:00Z")
            thumbnails = ThumbnailDetails().apply {
                high = Thumbnail().apply {
                    url = faker.internet().url()
                    width = 800
                    height = 800
                }
            }
        }
        contentDetails = ChannelContentDetails().apply {
            relatedPlaylists = RelatedPlaylists().apply {
                uploads = faker.youtube().playlistId()
            }
        }
        statistics = ChannelStatistics().apply {
            viewCount = faker.number().numberBetween(0, 10_000).toBigInteger()
            subscriberCount = faker.number().numberBetween(0, 10_000).toBigInteger()
            videoCount = faker.number().numberBetween(0, 100).toBigInteger()
        }
        brandingSettings = ChannelBrandingSettings().apply {
            channel = ChannelSettings().apply {
                keywords = "short keyword \"long keyword\""
            }
        }
    }
}

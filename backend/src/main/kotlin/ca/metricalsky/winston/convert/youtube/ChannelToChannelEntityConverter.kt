package ca.metricalsky.winston.convert.youtube

import ca.metricalsky.winston.database.entity.channel.ChannelEntity
import com.google.api.services.youtube.model.Channel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class ChannelToChannelEntityConverter: Converter<Channel, ChannelEntity> {

    @Autowired
    private lateinit var conversionService: ConversionService

    val keywordRegex = Regex("\".+?\"|[^ ]+")

    override fun convert(source: Channel) = ChannelEntity(
        id = source.id,
        title = source.snippet?.title,
        description = source.snippet?.description,
        customUrl = source.snippet?.customUrl,
        thumbnailUrl = source.snippet?.thumbnails?.high?.url,
        uploadsPlaylistId = source.contentDetails?.relatedPlaylists?.uploads,
        videoCount = source.statistics?.videoCount?.toLong(),
        viewCount = source.statistics?.viewCount?.toLong(),
        subscriberCount = source.statistics?.subscriberCount?.toLong(),
        publishedAt = conversionService.convert(source.snippet?.publishedAt, OffsetDateTime::class.java),
        topics = source.topicDetails?.topicCategories.orEmpty().toSet(),
        keywords = convertKeywords(source.brandingSettings?.channel?.keywords),
    )

    private fun convertKeywords(keywords: String?) = when (keywords) {
        null -> emptySet()
        else -> keywordRegex.findAll(keywords).map { it.value.trimQuotes() }.toSet()
    }

    private fun String.trimQuotes() = when {
        startsWith('"') && endsWith('"') -> substring(1, length - 1)
        else -> this
    }
}

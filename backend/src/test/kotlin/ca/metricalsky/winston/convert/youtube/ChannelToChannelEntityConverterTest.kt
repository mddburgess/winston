package ca.metricalsky.winston.convert.youtube

import ca.metricalsky.winston.database.entity.channel.ChannelEntity
import ca.metricalsky.winston.test.annotation.ConverterTest
import ca.metricalsky.winston.test.faker.WinstonFaker
import com.google.api.services.youtube.model.Channel
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.core.convert.ConversionService

@ConverterTest
class ChannelToChannelEntityConverterTest(
    conversionService: ConversionService
): WordSpec({

    val faker = WinstonFaker()

    "convert()" should {

        "convert an empty object" {
            val channel = Channel()
            val channelEntity = conversionService.convert(channel, ChannelEntity::class.java)

            channelEntity shouldNotBeNull {
                id shouldBe null
                title shouldBe null
                description shouldBe null
                customUrl shouldBe null
                thumbnailUrl shouldBe null
                uploadsPlaylistId shouldBe null
                videoCount shouldBe null
                viewCount shouldBe null
                subscriberCount shouldBe null
                publishedAt shouldBe null
                properties shouldBe null
                topics shouldBe emptySet()
                keywords shouldBe emptySet()
            }
        }

        "convert a complete object" {
            val channel = faker.youtube().channel().completeObject()
            val channelEntity = conversionService.convert(channel, ChannelEntity::class.java)

            channelEntity shouldNotBeNull {
                id shouldBe channel.id
                title shouldBe channel.snippet.title
                description shouldBe channel.snippet.description
                customUrl shouldBe channel.snippet.customUrl
                thumbnailUrl shouldBe channel.snippet.thumbnails.high.url
                uploadsPlaylistId shouldBe channel.contentDetails.relatedPlaylists.uploads
                publishedAt shouldNotBe null
                videoCount shouldBe channel.statistics.videoCount.toLong()
                viewCount shouldBe channel.statistics.viewCount.toLong()
                subscriberCount shouldBe channel.statistics.subscriberCount.toLong()
                properties shouldBe null
                topics shouldBe emptySet()
                keywords shouldBe emptySet()
            }
        }
    }
})

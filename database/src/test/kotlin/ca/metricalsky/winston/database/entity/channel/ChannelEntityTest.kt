package ca.metricalsky.winston.database.entity.channel

import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.date.shouldHaveSameInstantAs
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class ChannelEntityTest(
    entityManager: TestEntityManager
): StringSpec({

    val faker = DatabaseFaker()

    "entity persists with only required fields" {
        val channel = faker.channel().minimal()

        entityManager.persistFlushFind(channel) shouldNotBeNull {
            id shouldBe channel.id
            title shouldBe null
            description shouldBe null
            customUrl shouldBe null
            thumbnailUrl shouldBe null
            uploadsPlaylistId shouldBe null
            videoCount shouldBe null
            viewCount shouldBe null
            subscriberCount shouldBe null
            publishedAt shouldBe null
            lastFetchedAt shouldNotBe null
            properties shouldBe null
            topics shouldBe emptySet()
            keywords shouldBe emptySet()
        }
    }

    "entity persists with all optional fields" {
        val channel = faker.channel().complete()

        entityManager.persistFlushFind(channel) shouldNotBeNull {
            id shouldBe channel.id
            title shouldBe channel.title
            description shouldBe channel.description
            customUrl shouldBe channel.customUrl
            thumbnailUrl shouldBe channel.thumbnailUrl
            uploadsPlaylistId shouldBe channel.uploadsPlaylistId
            videoCount shouldBe channel.videoCount
            viewCount shouldBe channel.viewCount
            subscriberCount shouldBe channel.subscriberCount
            publishedAt shouldBe channel.publishedAt
            lastFetchedAt shouldHaveSameInstantAs channel.lastFetchedAt
            properties shouldBe null
            topics shouldContainExactly channel.topics
            keywords shouldContainExactly channel.keywords
        }
    }
})

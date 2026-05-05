package ca.metricalsky.winston.database.repository.channel

import ca.metricalsky.winston.database.entity.channel.ChannelEntity
import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class ChannelRepositoryTest(
    channelRepository: ChannelRepository,
    entityManager: TestEntityManager,
): WordSpec({

    val faker = DatabaseFaker()
    var channel: ChannelEntity? = null

    beforeEach {
        channel = entityManager.persist(faker.channel().completeEntity())
    }

    "findIdByCustomUrl()" should {

        "return a channel when given its custom URL" {
            val result = channelRepository.findIdByCustomUrl(channel?.customUrl!!)
            result shouldBe channel.id
        }

        "return null when given an invalid custom URL" {
            val result = channelRepository.findIdByCustomUrl("@notFound")
            result shouldBe null
        }
    }
})

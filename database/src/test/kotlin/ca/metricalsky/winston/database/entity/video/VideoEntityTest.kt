package ca.metricalsky.winston.database.entity.video

import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class VideoEntityTest(
    entityManager: TestEntityManager,
): WordSpec({

    val faker = DatabaseFaker()

    "VideoEntity" should {

        "persist with only required fields" {
            val channel = entityManager.persist(faker.channel().minimalEntity())
            val video = faker.video().minimalEntity(channel)

            entityManager.persistFlushFind(video) shouldNotBeNull {
                id shouldNotBe null
                channelId shouldBe video.channelId
                title shouldBe null
                description shouldBe null
                thumbnailUrl shouldBe null
                publishedAt shouldBe null
                lastFetchedAt shouldNotBe null
                details shouldBe null
                comments shouldBe null
            }
        }

        "persist with all optional fields" {
            val channel = entityManager.persist(faker.channel().minimalEntity())
            val video = faker.video().completeEntity(channel)

            entityManager.persistFlushFind(video) shouldNotBeNull {
                id shouldNotBe null
                channelId shouldBe video.channelId
                title shouldBe video.title
                description shouldBe video.description
                thumbnailUrl shouldBe video.thumbnailUrl
                publishedAt shouldNotBe null
                lastFetchedAt shouldNotBe null
                details shouldBe null
                comments shouldBe null
            }
        }
    }
})

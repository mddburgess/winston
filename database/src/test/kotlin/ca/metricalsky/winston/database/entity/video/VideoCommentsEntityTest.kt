package ca.metricalsky.winston.database.entity.video

import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class VideoCommentsEntityTest(
    entityManager: TestEntityManager,
): WordSpec({

    val faker = DatabaseFaker()

    "VideoCommentsEntity" should {

        "persist" {
            val channel = entityManager.persist(faker.channel().minimalEntity())
            val video = entityManager.persist(faker.video().minimalEntity(channel))
            val videoComments = faker.videoComments().completeEntity(video)

            entityManager.persistFlushFind(videoComments) shouldNotBeNull {
                this shouldNotBeSameInstanceAs videoComments
                videoId shouldBe videoComments.videoId
                commentsDisabled shouldBe videoComments.commentsDisabled
                commentCount shouldBe videoComments.commentCount
                replyCount shouldBe videoComments.replyCount
                totalReplyCount shouldBe videoComments.totalReplyCount
                lastFetchedAt shouldNotBe null
            }
        }
    }
})

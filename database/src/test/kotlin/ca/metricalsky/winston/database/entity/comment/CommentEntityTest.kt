package ca.metricalsky.winston.database.entity.comment

import ca.metricalsky.winston.database.entity.video.VideoEntity
import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import ca.metricalsky.winston.database.test.jpa.ext.refreshAll
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class CommentEntityTest(
    entityManager: TestEntityManager,
) : WordSpec({

    val faker = DatabaseFaker()
    var video: VideoEntity? = null

    beforeEach {
        val channel = entityManager.persist(faker.channel().minimalEntity())
        video = entityManager.persist(faker.video().minimalEntity(channel))
    }

    "CommentEntity" should {

        "persist with only required fields" {
            val comment = faker.comment().minimalEntity(video)

            entityManager.persistFlushFind(comment) shouldNotBeNull {
                this shouldNotBeSameInstanceAs comment
                id shouldBe comment.id
                videoId shouldBe comment.videoId
                parentId shouldBe null
                author shouldBe null
                textDisplay shouldBe null
                textOriginal shouldBe null
                likeCount shouldBe null
                totalReplyCount shouldBe null
                publishedAt shouldBe null
                updatedAt shouldBe null
                lastFetchedAt shouldNotBe null
                properties shouldBe null
                replies shouldBe emptyList()
            }
        }

        "persist with all optional fields" {
            val comment = faker.comment().completeEntity(video)

            entityManager.persistFlushFind(comment) shouldNotBeNull {
                this shouldNotBeSameInstanceAs comment
                id shouldBe comment.id
                videoId shouldBe comment.videoId
                parentId shouldBe null
                author shouldBe null
                textDisplay shouldBe comment.textDisplay
                textOriginal shouldBe comment.textOriginal
                likeCount shouldBe comment.likeCount
                totalReplyCount shouldBe comment.totalReplyCount
                publishedAt shouldNotBe null
                updatedAt shouldNotBe null
                lastFetchedAt shouldNotBe null
                properties shouldBe null
                replies shouldBe emptyList()
            }
        }

        "persist replies" {
            val comment = faker.comment().minimalEntity(video)
            val reply = faker.comment().minimalEntity(video)
            comment.replies = mutableListOf(reply)

            entityManager.persistFlushFind(comment) shouldNotBeNull {
                this shouldNotBeSameInstanceAs comment

                entityManager.refreshAll(replies)
                replies shouldHaveSize 1
                replies[0] shouldNotBeNull {
                    parentId shouldBe comment.id
                }
            }
        }
    }
})

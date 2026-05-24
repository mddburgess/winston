package ca.metricalsky.winston.convert.youtube

import ca.metricalsky.winston.database.entity.comment.CommentEntity
import ca.metricalsky.winston.test.annotation.ConverterTest
import ca.metricalsky.winston.test.faker.WinstonFaker
import com.google.api.services.youtube.model.CommentThread
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.core.convert.ConversionService

@ConverterTest
class CommentThreadToCommentEntityConverterTest(
    conversionService: ConversionService,
): WordSpec({

    val faker = WinstonFaker()

    "convert()" should {

        "convert an empty object" {
            val commentThread = CommentThread()
            val commentEntity = conversionService.convert(commentThread, CommentEntity::class.java)

            commentEntity shouldBe null
        }

        "convert a complete object" {
            var commentThread = faker.youtube().commentThread().completeObject()
            var commentEntity = conversionService.convert(commentThread, CommentEntity::class.java)

            commentEntity shouldNotBeNull {
                id shouldBe commentThread.snippet.topLevelComment.id
                videoId shouldBe commentThread.snippet.topLevelComment.snippet.videoId
                parentId shouldBe null
                author shouldNotBeNull {
                    id shouldBe commentThread.snippet.topLevelComment.snippet.authorChannelId.value
                    displayName shouldBe commentThread.snippet.topLevelComment.snippet.authorDisplayName
                    channelUrl shouldBe commentThread.snippet.topLevelComment.snippet.authorChannelUrl
                    profileImageUrl shouldBe commentThread.snippet.topLevelComment.snippet.authorProfileImageUrl
                }
                textDisplay shouldBe commentThread.snippet.topLevelComment.snippet.textDisplay
                textOriginal shouldBe commentThread.snippet.topLevelComment.snippet.textOriginal
                likeCount shouldBe commentThread.snippet.topLevelComment.snippet.likeCount
                totalReplyCount shouldBe commentThread.snippet.totalReplyCount
                publishedAt shouldNotBe null
                updatedAt shouldNotBe null
                lastFetchedAt shouldBe null
                properties shouldBe null
                replies shouldHaveSize commentThread.replies.size
            }
        }
    }
})

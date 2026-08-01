package ca.metricalsky.winston.convert.youtube

import ca.metricalsky.winston.database.entity.comment.CommentEntity
import ca.metricalsky.winston.test.annotation.ConverterTest
import ca.metricalsky.winston.test.faker.WinstonFaker
import com.google.api.services.youtube.model.Comment
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotContain
import org.springframework.core.convert.ConversionService

@ConverterTest
class CommentToCommentEntityConverterTest(
    conversionService: ConversionService
): WordSpec({

    val faker = WinstonFaker()

    "convert()" should {

        "convert an empty object" {
            val comment = Comment()
            val commentEntity = conversionService.convert(comment, CommentEntity::class.java)

            commentEntity shouldNotBeNull {
                id shouldBe null
                videoId shouldBe null
                parentId shouldBe null
                author shouldNotBeNull {
                    id shouldBe null
                    displayName shouldBe null
                    channelUrl shouldBe null
                    profileImageUrl shouldBe null
                }
                textDisplay shouldBe null
                textOriginal shouldBe null
                likeCount shouldBe null
                totalReplyCount shouldBe null
                publishedAt shouldBe null
                updatedAt shouldBe null
                lastFetchedAt shouldBe null
                properties shouldBe null
                replies shouldBe emptyList()
            }
        }

        "convert a complete object" {
            val comment = faker.youtube().comment().completeReply()
            val commentEntity = conversionService.convert(comment, CommentEntity::class.java)

            commentEntity shouldNotBeNull {
                id shouldBe comment.id
                videoId shouldBe comment.snippet.videoId
                parentId shouldBe comment.snippet.parentId
                author shouldNotBeNull {
                    id shouldBe comment.snippet.authorChannelId.value
                    displayName shouldBe comment.snippet.authorDisplayName
                    channelUrl shouldBe comment.snippet.authorChannelUrl
                    profileImageUrl shouldBe comment.snippet.authorProfileImageUrl
                }
                textDisplay shouldBe comment.snippet.textDisplay
                textOriginal shouldBe comment.snippet.textOriginal
                likeCount shouldBe comment.snippet.likeCount
                totalReplyCount shouldBe null
                publishedAt shouldNotBe null
                updatedAt shouldNotBe null
                lastFetchedAt shouldBe null
                properties shouldBe null
                replies shouldBe emptyList()
            }
        }

        "sanitize the textOriginal property" {
            val comment = faker.youtube().comment().completeComment().apply {
                snippet.textOriginal += "\u0000"
            }
            val commentEntity = conversionService.convert(comment, CommentEntity::class.java)

            commentEntity shouldNotBeNull {
                textOriginal shouldNotContain "\u0000"
            }
        }
    }
})

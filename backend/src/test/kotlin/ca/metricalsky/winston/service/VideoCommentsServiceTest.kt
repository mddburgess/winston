package ca.metricalsky.winston.service

import ca.metricalsky.winston.database.entity.video.VideoCommentsEntity
import ca.metricalsky.winston.database.entity.video.VideoCommentsEntity.CommentStatus
import ca.metricalsky.winston.database.repository.comment.CommentRepository
import ca.metricalsky.winston.database.repository.video.VideoCommentsRepository
import ca.metricalsky.winston.database.view.CommentCountView
import ca.metricalsky.winston.test.faker.WinstonFaker
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class VideoCommentsServiceTest: WordSpec({

    val commentRepository = mockk<CommentRepository>()
    val videoCommentsRepository = mockk<VideoCommentsRepository>()

    val videoCommentsService = VideoCommentsService(commentRepository, videoCommentsRepository)

    val faker = WinstonFaker()
    val videoId = faker.database().video().id()

    "updateVideoComments()" should {

        "save comment counts for a newly fetched video" {
            val savedEntity = slot<VideoCommentsEntity>()

            every { videoCommentsRepository.findById(videoId) } returns Optional.empty()
            every { commentRepository.countCommentsForVideoId(videoId) } returns mockk<CommentCountView>() {
                every { comments } returns 2
                every { replies } returns 3
                every { totalReplies } returns 4
            }
            every { videoCommentsRepository.save(capture(savedEntity)) } answers { firstArg() }

            videoCommentsService.updateVideoComments(videoId)

            savedEntity.captured should {
                it.videoId shouldBe videoId
                it.commentStatus shouldBe CommentStatus.ENABLED
                it.commentCount shouldBe 2
                it.replyCount shouldBe 3
                it.totalReplyCount shouldBe 4
            }
        }

        "save comment counts for a previously fetched video" {
            val savedEntity = slot<VideoCommentsEntity>()

            every { videoCommentsRepository.findById(videoId) } returns Optional.of(VideoCommentsEntity(
                videoId = videoId,
                commentCount = 1,
                replyCount = 1,
                totalReplyCount = 1,
            ))
            every { commentRepository.countCommentsForVideoId(videoId) } returns mockk<CommentCountView>() {
                every { comments } returns 2
                every { replies } returns 3
                every { totalReplies } returns 4
            }
            every { videoCommentsRepository.save(capture(savedEntity)) } answers { firstArg() }

            videoCommentsService.updateVideoComments(videoId)

            savedEntity.captured should {
                it.videoId shouldBe videoId
                it.commentStatus shouldBe CommentStatus.ENABLED
                it.commentCount shouldBe 2
                it.replyCount shouldBe 3
                it.totalReplyCount shouldBe 4
            }
        }
    }

    "markVideoCommentStatus()" should {

        "mark comment status for a newly fetched video" {
            val savedEntity = slot<VideoCommentsEntity>()

            every { videoCommentsRepository.findById(videoId) } returns Optional.empty()
            every { videoCommentsRepository.save(capture(savedEntity)) } answers { firstArg() }

            videoCommentsService.markVideoCommentStatus(videoId, CommentStatus.DISABLED)

            savedEntity.captured should {
                it.videoId shouldBe videoId
                it.commentStatus shouldBe CommentStatus.DISABLED
                it.commentCount shouldBe 0
                it.replyCount shouldBe 0
                it.totalReplyCount shouldBe 0
            }
        }

        "mark comment status for a previously fetched video" {
            val savedEntity = slot<VideoCommentsEntity>()

            every { videoCommentsRepository.findById(videoId) } returns Optional.of(VideoCommentsEntity(
                videoId = videoId,
                commentCount = 1,
                replyCount = 1,
                totalReplyCount = 1,
            ))
            every { videoCommentsRepository.save(capture(savedEntity)) } answers { firstArg() }

            videoCommentsService.markVideoCommentStatus(videoId, CommentStatus.RESTRICTED)

            savedEntity.captured should {
                it.videoId shouldBe videoId
                it.commentStatus shouldBe CommentStatus.RESTRICTED
                it.commentCount shouldBe 1
                it.replyCount shouldBe 1
                it.totalReplyCount shouldBe 1
            }
        }
    }
})

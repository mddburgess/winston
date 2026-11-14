package ca.metricalsky.winston.client.exception

import ca.metricalsky.winston.exception.ErrorCode
import com.google.api.client.googleapis.testing.json.GoogleJsonResponseExceptionFactoryTesting.newMock
import com.google.api.client.json.gson.GsonFactory
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf

class YouTubeExceptionTest: WordSpec({

    val jsonFactory = GsonFactory()

    "youTubeException()" should {

        "handle a comments disabled exception" {
            val exception = newMock(jsonFactory, 403, "commentsDisabled")

            youTubeException(exception).shouldBeTypeOf<CommentsDisabledException> {
                it.problem shouldBe ErrorCode.COMMENTS_DISABLED.problem
                it.cause shouldBe exception
            }
        }

        "handle a forbidden exception on comment threads" {
            val exception = newMock(jsonFactory, 403, "forbidden")
            exception.details.errors[0].domain = "youtube.commentThread"

            youTubeException(exception).shouldBeTypeOf<CommentsRestrictedException> {
                it.problem shouldBe ErrorCode.COMMENTS_RESTRICTED.problem
                it.cause shouldBe exception
            }
        }

        "handle a forbidden exception on a different endpoint" {
            val exception = newMock(jsonFactory, 403, "forbidden")
            exception.details.message = "The requested operation is forbidden and cannot be completed."

            youTubeException(exception).shouldBeTypeOf<YouTubeException> {
                it.problem shouldBe ErrorCode.YOUTUBE_EXCEPTION.problem.apply {
                    detail = "The requested operation is forbidden and cannot be completed."
                }
                it.cause shouldBe exception
            }
        }

        "handle a processing failure exception" {
            val exception = newMock(jsonFactory, 400, "processingFailure")

            youTubeException(exception).shouldBeTypeOf<ProcessingFailureException> {
                it.problem shouldBe ErrorCode.PROCESSING_FAILURE.problem
                it.cause shouldBe exception
            }
        }

        "handle a quota exceeded exception" {
            val exception = newMock(jsonFactory, 429, "quotaExceeded")

            youTubeException(exception).shouldBeTypeOf<QuotaExceededException> {
                it.problem shouldBe ErrorCode.QUOTA_EXCEEDED.problem
                it.cause shouldBe exception
            }
        }

        "handle a video not found exception" {
            val exception = newMock(jsonFactory, 404, "videoNotFound")

            youTubeException(exception).shouldBeTypeOf<VideoNotFoundException> {
                it.problem shouldBe ErrorCode.VIDEO_NOT_FOUND.problem
                it.cause shouldBe exception
            }
        }

        "handle a generic YouTube exception" {
            val exception = newMock(jsonFactory, 400, "unknownApi")
            exception.details.message = "The API that the request is calling is not recognized."

            youTubeException(exception).shouldBeTypeOf<YouTubeException> {
                it.problem shouldBe ErrorCode.YOUTUBE_EXCEPTION.problem.apply {
                    detail = "The API that the request is calling is not recognized."
                }
                it.cause shouldBe exception
            }
        }

        "handle a runtime exception" {
            val exception = RuntimeException()

            youTubeException(exception).shouldBeTypeOf<YouTubeException> {
                it.problem shouldBe ErrorCode.YOUTUBE_EXCEPTION.problem
                it.cause shouldBe exception
            }
        }
    }
})

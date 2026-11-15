package ca.metricalsky.winston.client.exception

import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.WinstonException
import com.google.api.client.googleapis.json.GoogleJsonResponseException

open class YouTubeException(
    errorCode: ErrorCode = ErrorCode.YOUTUBE_EXCEPTION,
    cause: Throwable? = null,
) : WinstonException(errorCode, cause)

class CommentsDisabledException(cause: GoogleJsonResponseException? = null) :
    YouTubeException(ErrorCode.COMMENTS_DISABLED, cause)

class CommentsRestrictedException(cause: GoogleJsonResponseException? = null) :
    YouTubeException(ErrorCode.COMMENTS_RESTRICTED, cause)

class ProcessingFailureException(cause: GoogleJsonResponseException? = null) :
    YouTubeException(ErrorCode.PROCESSING_FAILURE, cause)

class QuotaExceededException(cause: GoogleJsonResponseException? = null) :
    YouTubeException(ErrorCode.QUOTA_EXCEEDED, cause)

class VideoNotFoundException(cause: GoogleJsonResponseException? = null) :
    YouTubeException(ErrorCode.VIDEO_NOT_FOUND, cause)

fun youTubeException(cause: Throwable) = when (cause) {
    is GoogleJsonResponseException -> when (cause.details?.errors?.firstOrNull()?.reason) {
        "commentsDisabled" -> CommentsDisabledException(cause)
        "forbidden" -> when (cause.details.errors.firstOrNull()?.domain) {
            "youtube.commentThread" -> CommentsRestrictedException(cause)
            else -> YouTubeException(cause = cause).apply {
                problem.detail = cause.details?.message ?: problem.detail
            }
        }
        "processingFailure" -> ProcessingFailureException(cause)
        "quotaExceeded" -> QuotaExceededException(cause)
        "videoNotFound" -> VideoNotFoundException(cause)
        else -> YouTubeException(cause = cause).apply {
            problem.detail = cause.details?.message ?: problem.detail
        }
    }
    else -> YouTubeException(cause = cause)
}

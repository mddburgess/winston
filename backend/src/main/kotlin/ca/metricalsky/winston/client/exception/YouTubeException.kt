package ca.metricalsky.winston.client.exception

import ca.metricalsky.winston.api.model.Problem
import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.WinstonException
import com.google.api.client.googleapis.json.GoogleJsonResponseException

open class YouTubeException protected constructor(
    problem: Problem,
    cause: Throwable? = null,
) : WinstonException(problem, cause) {

    constructor(
        errorCode: ErrorCode = ErrorCode.YOUTUBE_EXCEPTION,
        cause: Throwable? = null,
    ) : this(errorCode.problem, cause)
}

class CommentsDisabledException(cause: GoogleJsonResponseException? = null) :
    YouTubeException(ErrorCode.COMMENTS_DISABLED, cause)

class QuotaExceededException(cause: GoogleJsonResponseException? = null) :
    YouTubeException(ErrorCode.QUOTA_EXCEEDED, cause)

class VideoNotFoundException(cause: GoogleJsonResponseException? = null) :
    YouTubeException(ErrorCode.VIDEO_NOT_FOUND, cause)

fun wrap(cause: Throwable) = when (cause) {
    is GoogleJsonResponseException -> when (cause.details?.errors?.firstOrNull()?.reason) {
        "commentsDisabled" -> CommentsDisabledException(cause)
        "quotaExceeded" -> QuotaExceededException(cause)
        "videoNotFound" -> VideoNotFoundException(cause)
        else -> YouTubeException(cause = cause).also {
            it.problem.detail = cause.details.message
        }
    }
    else -> YouTubeException(cause = cause)
}

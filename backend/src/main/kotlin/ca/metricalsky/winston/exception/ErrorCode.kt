package ca.metricalsky.winston.exception

import ca.metricalsky.winston.api.model.Problem
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import java.net.URI

enum class ErrorCode(
    val status: HttpStatus,
    val detail: String,
) {
    AUTHOR_NOT_FOUND(HttpStatus.NOT_FOUND,
        "The requested author was not found."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND,
        "The requested channel was not found."),
    CHANNEL_NOT_PULLED(HttpStatus.UNPROCESSABLE_ENTITY,
        "The specified channel must be pulled before videos for that channel may be pulled."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,
        "The requested comment was not found."),
    COMMENTS_DISABLED(HttpStatus.UNPROCESSABLE_ENTITY,
        "Comments are disabled for the requested video."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST,
        "The request body is invalid and cannot be processed."),
    MALFORMED_REQUEST_BODY(HttpStatus.BAD_REQUEST,
        "The request body is malformed and cannot be read."),
    QUOTA_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS,
        "The YouTube API request quota has been exceeded."),
    REQUEST_TOO_EXPENSIVE(HttpStatus.UNPROCESSABLE_ENTITY,
        "The estimated cost for this request exceeds the available YouTube API request quota."),
    SERVICE_SHUTDOWN(HttpStatus.SERVICE_UNAVAILABLE,
        "The service is shutting down and is no longer available."),
    SUBSCRIPTION_CLOSED(HttpStatus.BAD_REQUEST,
        "The specified event subscription is not open."),
    VIDEO_NOT_FOUND(HttpStatus.NOT_FOUND,
        "The requested video was not found."),
    ;

    val type = "/api/problem/" + name.lowercase().replace("_", "-")

    val problemDetail: ProblemDetail
        get() {
            val problemDetail = ProblemDetail.forStatusAndDetail(status, detail)
            problemDetail.setType(URI.create(type))
            return problemDetail
        }

    val problem: Problem
        get() = Problem().also {
            it.type = type
            it.title = status.reasonPhrase
            it.status = status.value()
            it.detail = detail
        }
}

package ca.metricalsky.winston.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.util.Locale;

@Getter
public enum ErrorCode {

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

    private final String type;
    private final HttpStatus status;
    private final String detail;

    ErrorCode(HttpStatus status, String detail) {
        this.type = "/api/problem/" + name().toLowerCase(Locale.ENGLISH).replace("_", "-");
        this.status = status;
        this.detail = detail;
    }

    public ProblemDetail getProblemDetail() {
        var problemDetail = ProblemDetail.forStatusAndDetail(this.status, this.detail);
        problemDetail.setType(URI.create(this.type));
        return problemDetail;
    }
}

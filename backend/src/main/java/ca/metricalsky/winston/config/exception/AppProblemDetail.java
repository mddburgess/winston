package ca.metricalsky.winston.config.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.ErrorResponseException;

@Getter
@Setter
public class AppProblemDetail extends ProblemDetail {

    private ProblemException exception;

    protected AppProblemDetail(int rawStatusCode) {
        super(rawStatusCode);
    }

    protected AppProblemDetail(ProblemDetail other) {
        super(other);
    }

    protected AppProblemDetail() {
        super();
    }

    public static AppProblemDetail forStatus(HttpStatusCode status) {
        Assert.notNull(status, "HttpStatusCode is required");
        return forStatus(status.value());
    }

    public static AppProblemDetail forStatus(int status) {
        return new AppProblemDetail(status);
    }

    public static AppProblemDetail forException(@NonNull Throwable ex) {
        var appProblemDetail = ex instanceof ErrorResponseException errorResponse
                ? new AppProblemDetail(errorResponse.getBody())
                : forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        appProblemDetail.setException(ProblemException.forException(ex));
        return appProblemDetail;
    }
}

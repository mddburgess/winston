package ca.metricalsky.winston.exception

import ca.metricalsky.winston.api.model.Problem
import org.springframework.http.HttpStatus

open class WinstonException(
    val problem: Problem,
    cause: Throwable? = null,
) : RuntimeException(cause) {

    val status: HttpStatus = HttpStatus.valueOf(problem.status)

    constructor(
        errorCode: ErrorCode,
        cause: Throwable? = null,
    ) : this(errorCode.problem, cause)

    @Deprecated(message = "Java compatability only")
    constructor(errorCode: ErrorCode) : this(errorCode, null)

    override val message
        get() = "[${status.value()} ${status.reasonPhrase}] ${problem.detail}"
}

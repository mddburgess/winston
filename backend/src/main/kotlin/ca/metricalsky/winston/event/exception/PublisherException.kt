package ca.metricalsky.winston.event.exception

import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.WinstonException

class PublisherException(
    errorCode: ErrorCode,
    cause: Throwable? = null,
) : WinstonException(errorCode, cause)

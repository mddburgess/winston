package ca.metricalsky.winston.api.ext

import ca.metricalsky.winston.api.model.ProblemException
import ca.metricalsky.winston.api.model.ProblemExceptionCause

fun ProblemException(ex: Throwable?) = when (ex) {
    null -> null
    else -> ProblemException().also {
        val stackTrace = ex.stackTrace.map { trace -> trace.toString() }

        it.propertyClass = ex.javaClass.canonicalName
        it.message = ex.localizedMessage
        it.trace = stackTrace
        it.causes = buildCauses(ex.cause, stackTrace).ifEmpty { null }
    }
}

private fun buildCauses(ex: Throwable?, parentStackTrace: List<String>): List<ProblemExceptionCause> {
    if (ex == null) {
        return emptyList()
    }

    val stackTrace = ex.stackTrace.map { it.toString() }
    val cause = ProblemExceptionCause().also {
        it.propertyClass = ex.javaClass.canonicalName
        it.message = ex.localizedMessage
        it.trace = truncateStackTrace(stackTrace, parentStackTrace)
    }
    return listOf(cause) + buildCauses(ex.cause, stackTrace)
}

private fun truncateStackTrace(stackTrace: List<String>, parentStackTrace: List<String>): List<String> {
    var lastIndex = stackTrace.size - 1
    var lastParentIndex = parentStackTrace.size - 1
    while (lastIndex >= 0 && lastParentIndex >= 0 && stackTrace[lastIndex] == parentStackTrace[lastParentIndex]) {
        lastIndex--
        lastParentIndex--
    }

    val truncatedStackTrace = stackTrace.subList(0, lastIndex + 1).toMutableList()
    if (lastIndex < stackTrace.size - 1) {
        val truncatedLength = stackTrace.size - 1 - lastIndex
        truncatedStackTrace.add("... ${truncatedLength} more")
    }
    return truncatedStackTrace
}

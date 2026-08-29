package ca.metricalsky.winston.exception.handlers

import ca.metricalsky.winston.api.model.Problem

interface ExceptionHandler<T: Throwable> {

    fun handleException(exception: T): Problem
}

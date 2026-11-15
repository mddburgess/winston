package ca.metricalsky.winston.utils.ext

import java.util.Optional

infix fun <T> Optional<T>?.orElse(default: T): T = when {
    this == null -> default
    this.isEmpty -> default
    else -> get()
}

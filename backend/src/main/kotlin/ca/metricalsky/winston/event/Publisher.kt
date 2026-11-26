package ca.metricalsky.winston.event

import ca.metricalsky.winston.api.model.Event

interface Publisher {

    fun publish(event: Event)
}

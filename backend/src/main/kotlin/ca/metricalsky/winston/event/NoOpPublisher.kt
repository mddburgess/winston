package ca.metricalsky.winston.event

import ca.metricalsky.winston.api.model.Event

class NoOpPublisher : Publisher {

    override fun publish(event: Event) {}
}

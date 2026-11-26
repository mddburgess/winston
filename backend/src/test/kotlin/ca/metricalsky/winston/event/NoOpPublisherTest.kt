package ca.metricalsky.winston.event

import ca.metricalsky.winston.api.model.Event
import io.kotest.core.spec.style.WordSpec

class NoOpPublisherTest : WordSpec({

    val publisher = NoOpPublisher()

    "publish()" should {

        "do nothing" {
            publisher.publish(Event())
        }
    }
})

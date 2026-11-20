package ca.metricalsky.winston.event

import ca.metricalsky.winston.api.model.Event
import io.kotest.core.spec.style.WordSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.springframework.core.convert.ConversionService

class EventPublisherServiceTest : WordSpec({

    val conversionService = mockk<ConversionService>()

    val eventPublisherService = EventPublisherService(conversionService)

    "publishEvent()" should {

        val appEvent = mockk<Event>()
        every { conversionService.convert(any(), Event::class.java) } returns appEvent

        "publish an event to the registered event publisher" {
            val publisher = mockk<Publisher>()
            every { publisher.publish(any()) } just runs

            eventPublisherService.registerPublisher(publisher)
            eventPublisherService.publishEvent(appEvent)

            verify { publisher.publish(appEvent) }
        }

        "publish an event to the default event publisher" {
            eventPublisherService.publishEvent(Any())
        }
    }
})

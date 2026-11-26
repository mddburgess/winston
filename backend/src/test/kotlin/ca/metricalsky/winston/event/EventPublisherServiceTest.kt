package ca.metricalsky.winston.event

import ca.metricalsky.winston.api.model.Event
import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.WinstonException
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.springframework.core.convert.ConversionService

class EventPublisherServiceTest : WordSpec({

    val conversionService = mockk<ConversionService>()

    val eventPublisherService = EventPublisherService(conversionService)

    "registerPublisher()" should {

        afterEach {
            eventPublisherService.unregisterPublisher()
        }

        "set the thread-local publisher" {
            val publisher = mockk<Publisher>()

            eventPublisherService.registerPublisher(publisher)

            eventPublisherService.publisher shouldBe publisher
        }
    }

    "unregisterPublisher()" should {

        beforeEach {
            val publisher = mockk<Publisher>()
            eventPublisherService.registerPublisher(publisher)
        }

        "reset the thread-local publisher" {
            eventPublisherService.unregisterPublisher()

            eventPublisherService.publisher.shouldBeTypeOf<NoOpPublisher>()
        }
    }

    "publishEvent()" should {

        val event = Event()
        every { conversionService.convert(any(), Event::class.java) } returns event

        val publisher = mockk<Publisher>()
        every { publisher.publish(any()) } just runs

        beforeEach {
            eventPublisherService.registerPublisher(publisher)
        }

        afterEach {
            eventPublisherService.unregisterPublisher()
        }

        "publish an event to the registered event publisher" {
            eventPublisherService.publishEvent(event)

            verify { publisher.publish(event) }
        }

        "publish an event with an application exception" {
            val exception = WinstonException(ErrorCode.INVALID_REQUEST_BODY)

            eventPublisherService.publishEvent(event, exception)

            event.error shouldBe exception.problem
            verify { publisher.publish(event) }
        }

        "publish an event with a runtime exception" {
            val exception = WinstonException(ErrorCode.RUNTIME_EXCEPTION, RuntimeException())

            eventPublisherService.publishEvent(event, RuntimeException())

            event.error shouldBe exception.problem
            verify { publisher.publish(event) }
        }
    }
})

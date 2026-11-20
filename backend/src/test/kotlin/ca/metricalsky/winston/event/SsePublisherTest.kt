package ca.metricalsky.winston.event

import ca.metricalsky.winston.api.model.Event
import ca.metricalsky.winston.event.exception.PublisherException
import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.WinstonException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder
import java.io.IOException
import java.util.UUID

class SsePublisherTest : WordSpec({

    val sseEmitter = mockk<SseEmitter> {
        every { onCompletion(any()) } just runs
        every { onTimeout(any()) } just runs
        every { onError(any()) } just runs
        every { send(any<SseEventBuilder>()) } just runs
        every { complete() } just runs
        every { completeWithError(any()) } just runs
    }

    val ssePublisher = SsePublisher(sseEmitter)

    "complete()" should {

        "call complete() on the SseEmitter" {
            ssePublisher.complete()

            verify { sseEmitter.complete() }
        }
    }

    "completeWithError()" should {

        "call completeWithError() on the SseEmitter" {
            val exception = WinstonException(ErrorCode.RUNTIME_EXCEPTION)

            ssePublisher.completeWithError(exception)

            verify { sseEmitter.completeWithError(exception) }
        }
    }

    "publish()" should {

        "throw a PublisherException when an I/O error occurs" {
            every { sseEmitter.send(any<SseEventBuilder>()) } throws IOException()

            shouldThrow<PublisherException> {
                ssePublisher.publish(Event().eventId(UUID.randomUUID()))
            } should {
                it.problem shouldBe ErrorCode.RUNTIME_EXCEPTION.problem
                it.cause.shouldBeTypeOf<IOException>()
            }
        }
    }
})

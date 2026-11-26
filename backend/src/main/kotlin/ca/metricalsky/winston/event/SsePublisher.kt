package ca.metricalsky.winston.event

import ca.metricalsky.winston.api.model.Event
import ca.metricalsky.winston.api.model.EventSubscriptionEvent
import ca.metricalsky.winston.event.exception.PublisherException
import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.WinstonException
import org.springframework.http.MediaType
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder
import java.util.UUID

class SsePublisher internal constructor(
    val sseEmitter: SseEmitter,
) : Publisher {

    val id: UUID = UUID.randomUUID()
    internal var open: Boolean = true

    init {
        sseEmitter.apply {
            onCompletion { open = false }
            onTimeout { open = false }
            onError { open = false }
        }
    }

    constructor(timeout: Long) : this(SseEmitter(timeout))

    fun complete() {
        publish(
            EventSubscriptionEvent()
                .eventType("event-subscription")
                .eventSubscriptionId(id)
                .subscribed(false)
        )
        sseEmitter.complete()
    }

    fun completeWithError(ex: WinstonException) {
        if (open) {
            publish(
                EventSubscriptionEvent()
                    .eventType("event-subscription")
                    .eventSubscriptionId(id)
                    .subscribed(false)
                    .error(ex.problem)
            )
        }
        sseEmitter.completeWithError(ex)
    }

    override fun publish(event: Event) {
        event.eventId = UUID.randomUUID()
        publish(
            SseEmitter.event()
                .id(event.eventId.toString())
                .name(event.eventType)
                .data(event, MediaType.APPLICATION_JSON)
        )
    }


    private fun publish(builder: SseEventBuilder) = when {
        open -> try {
            sseEmitter.send(builder);
        } catch (ex: Exception) {
            throw PublisherException(ErrorCode.RUNTIME_EXCEPTION, ex)
        }

        else -> throw PublisherException(ErrorCode.PUBLISHER_CLOSED)
    }
}

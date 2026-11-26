package ca.metricalsky.winston.event

import ca.metricalsky.winston.api.model.Event
import ca.metricalsky.winston.exception.ErrorCode
import ca.metricalsky.winston.exception.WinstonException
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service

@Service
class EventPublisherService(
    private val conversionService: ConversionService,
) {
    private val threadLocalPublisher: ThreadLocal<Publisher> = ThreadLocal.withInitial { NoOpPublisher() }

    internal val publisher: Publisher
        get() = threadLocalPublisher.get()

    fun registerPublisher(publisher: Publisher) {
        threadLocalPublisher.set(publisher)
    }

    fun unregisterPublisher() {
        threadLocalPublisher.remove()
    }

    fun publishEvent(entity: Any, throwable: Throwable? = null) {
        val event = conversionService.convert(entity, Event::class.java)!!
        if (throwable is WinstonException) {
            event.error = throwable.problem
        } else if (throwable != null) {
            event.error = WinstonException(ErrorCode.RUNTIME_EXCEPTION, throwable).problem
        }

        publisher.publish(event)
    }
}

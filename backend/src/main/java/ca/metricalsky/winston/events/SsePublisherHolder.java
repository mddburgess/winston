package ca.metricalsky.winston.events;

import ca.metricalsky.winston.utils.PrototypeScope;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
public class SsePublisherHolder {

    private final ThreadLocal<SsePublisher> threadLocalSsePublisher = new ThreadLocal<>();

    @Bean
    @PrototypeScope
    public SsePublisher ssePublisher() {
        return requireNonNull(threadLocalSsePublisher.get(),
                "The SsePublisher must be provided before it can be used.");
    }

    public void hold(@NonNull SsePublisher ssePublisher) {
        threadLocalSsePublisher.set(requireNonNull(ssePublisher,
                "The provided SsePublisher must not be null."));
    }

    public void clear() {
        threadLocalSsePublisher.remove();
    }
}

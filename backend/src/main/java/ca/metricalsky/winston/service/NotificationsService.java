package ca.metricalsky.winston.service;

import ca.metricalsky.winston.config.exception.AppProblemDetail;
import ca.metricalsky.winston.events.SubscriptionEvent;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.events.SsePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class NotificationsService implements SmartLifecycle {

    private static final Long DEFAULT_TIMEOUT_MS = 120_000L;

    private final Map<UUID, SsePublisher> subscriptions = new ConcurrentHashMap<>();
    private boolean running;

    public SsePublisher openSubscription() {
        return openSubscription(DEFAULT_TIMEOUT_MS);
    }

    public SsePublisher openSubscription(Long timeout) {
        var ssePublisher = new SsePublisher(timeout);
        var subscriptionId = ssePublisher.getId();
        var sseEmitter = ssePublisher.getSseEmitter();
        sseEmitter.onCompletion(() -> {
            log.info("Notifications subscription {} completed", subscriptionId);
            subscriptions.remove(subscriptionId);
        });
        sseEmitter.onTimeout(() -> {
            log.warn("Notifications subscription {} timed out", subscriptionId);
            subscriptions.remove(subscriptionId);
        });
        sseEmitter.onError(exception -> {
            log.error("Notifications subscription {} failed", subscriptionId, exception);
            subscriptions.remove(subscriptionId);
        });
        subscriptions.put(subscriptionId, ssePublisher);

        log.info("Notifications subscription {} opened", subscriptionId);
        ssePublisher.publish(new SubscriptionEvent(true, subscriptionId));
        return ssePublisher;
    }

    public SsePublisher requireSubscription(UUID subscriptionId) {
        return Optional.ofNullable(subscriptions.get(subscriptionId))
                .orElseThrow(() -> new AppException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "The provided subscription stream is not open."));
    }

    @Override
    public void start() {
        running = true;
    }

    @Override
    public void stop() {
        if (!subscriptions.isEmpty()) {
            var ex = new AppException(HttpStatus.SERVICE_UNAVAILABLE,
                    "The server is shutting down and is no longer available.");
            Map.copyOf(subscriptions).forEach((subscriptionId, ssePublisher) -> {
                log.warn("Closing notifications subscription {}", subscriptionId);
                ssePublisher.publish(AppProblemDetail.forException(ex));
                ssePublisher.completeWithError(ex);
            });
        }
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}

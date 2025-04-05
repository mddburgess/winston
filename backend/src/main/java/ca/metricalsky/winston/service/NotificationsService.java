package ca.metricalsky.winston.service;

import ca.metricalsky.winston.events.SubscriptionEvent;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.utils.SsePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class NotificationsService {

    private static final Long DEFAULT_TIMEOUT_MS = 30_000L;

    private final Map<UUID, SseEmitter> subscriptions = new ConcurrentHashMap<>();

    public SsePublisher openSubscription() throws IOException {
        return openSubscription(DEFAULT_TIMEOUT_MS);
    }

    public SsePublisher openSubscription(Long timeout) throws IOException {
        var subscriptionId = UUID.randomUUID();
        var sseEmitter = new SseEmitter(timeout);
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
        subscriptions.put(subscriptionId, sseEmitter);

        log.info("Notifications subscription {} opened", subscriptionId);
        sseEmitter.send(new SubscriptionEvent(true, subscriptionId), MediaType.APPLICATION_JSON);
        return new SsePublisher(sseEmitter);
    }

    public SsePublisher requireSubscription(UUID subscriptionId) {
        return Optional.ofNullable(subscriptions.get(subscriptionId))
                .map(SsePublisher::new)
                .orElseThrow(() -> new AppException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "The provided subscription stream is not open."));
    }
}

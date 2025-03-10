package ca.metricalsky.yt.comments.service;

import ca.metricalsky.yt.comments.events.SubscriptionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

    public SseEmitter openSubscription() throws IOException {
        return openSubscription(DEFAULT_TIMEOUT_MS);
    }

    public SseEmitter openSubscription(Long timeout) throws IOException {
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
        return sseEmitter;
    }

    public SseEmitter requireSubscription(UUID subscriptionId) {
        return Optional.ofNullable(subscriptions.get(subscriptionId)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "The provided subscription stream is not open."));
    }
}

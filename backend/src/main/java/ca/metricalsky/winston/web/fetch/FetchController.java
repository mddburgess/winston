package ca.metricalsky.winston.web.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.service.NotificationsService;
import ca.metricalsky.winston.service.fetch.AsyncFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FetchController {

    private final NotificationsService notificationsService;
    private final AsyncFetchService asyncFetchService;

    @PostMapping("/api/fetch")
    public ResponseEntity<SseEmitter> fetch(
            @RequestHeader(value = "X-Notify-Subscription", required = false) UUID subscriptionId,
            @RequestBody FetchRequestDto request
    ) {
        var ssePublisher = subscriptionId == null
                ? notificationsService.openSubscription()
                : notificationsService.requireSubscription(subscriptionId);

        asyncFetchService.fetch(request, ssePublisher);

        return subscriptionId == null
                ? ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(ssePublisher.getSseEmitter())
                : ResponseEntity.accepted().build();
    }
}

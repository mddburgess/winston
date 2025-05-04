package ca.metricalsky.winston.web.fetch;

import ca.metricalsky.winston.dto.fetch.FetchLimitsResponse;
import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.service.NotificationsService;
import ca.metricalsky.winston.service.fetch.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fetch")
public class FetchController {

    private final NotificationsService notificationsService;
    private final FetchService fetchService;

    @PostMapping
    public ResponseEntity<SseEmitter> fetch(
            @RequestHeader(value = "X-Notify-Subscription", required = false) UUID subscriptionId,
            @RequestBody FetchRequestDto request
    ) {
        var ssePublisher = subscriptionId == null
                ? notificationsService.openSubscription()
                : notificationsService.requireSubscription(subscriptionId);

        fetchService.fetchAsync(request, ssePublisher);

        return subscriptionId == null
                ? ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(ssePublisher.getSseEmitter())
                : ResponseEntity.accepted().build();
    }

    @GetMapping("/limits")
    public FetchLimitsResponse getFetchLimits() {
        var remainingQuota = fetchService.getRemainingQuota();
        return new FetchLimitsResponse(remainingQuota);
    }
}

package ca.metricalsky.winston.web.fetch;

import ca.metricalsky.winston.api.FetchApi;
import ca.metricalsky.winston.api.model.FetchLimitsResponse;
import ca.metricalsky.winston.api.model.FetchRequest;
import ca.metricalsky.winston.events.FetchDataEventBuilder;
import ca.metricalsky.winston.service.NotificationsService;
import ca.metricalsky.winston.service.fetch.FetchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FetchController implements FetchApi {

    private final NotificationsService notificationsService;
    private final FetchService fetchService;

    @Override
    public ResponseEntity<Void> fetch(UUID xNotifySubscription, FetchRequest fetchRequest) {
        var fetchRequestId = fetchService.save(fetchRequest);
        var ssePublisher = notificationsService.requireSubscription(xNotifySubscription);
        ssePublisher.setEventBuilder(new FetchDataEventBuilder());
        fetchService.fetchAsync(fetchRequestId, ssePublisher);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/api/dev/fetch")
    public ResponseEntity<SseEmitter> fetchDev(@Valid @RequestBody FetchRequest fetchRequest) {
        var fetchRequestId = fetchService.save(fetchRequest);
        var ssePublisher = notificationsService.openSubscription();
        ssePublisher.setEventBuilder(new FetchDataEventBuilder());
        fetchService.fetchAsync(fetchRequestId, ssePublisher);

        return ResponseEntity.ok(ssePublisher.getSseEmitter());
    }

    @Override
    public ResponseEntity<FetchLimitsResponse> getFetchLimits() {
        var remainingQuota = fetchService.getRemainingQuota();
        var response = new FetchLimitsResponse()
                .remainingQuota(remainingQuota);

        return ResponseEntity.ok(response);
    }
}

package ca.metricalsky.winston.web.fetch;

import ca.metricalsky.winston.api.FetchApi;
import ca.metricalsky.winston.api.model.FetchLimitsResponse;
import ca.metricalsky.winston.api.model.FetchRequest;
import ca.metricalsky.winston.service.NotificationsService;
import ca.metricalsky.winston.service.fetch.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
        fetchService.fetchAsync(fetchRequestId, ssePublisher);

        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<FetchLimitsResponse> getFetchLimits() {
        var remainingQuota = fetchService.getRemainingQuota();
        var response = new FetchLimitsResponse()
                .remainingQuota(remainingQuota);

        return ResponseEntity.ok(response);
    }
}

package ca.metricalsky.winston.web.fetch;

import ca.metricalsky.winston.api.FetchApi;
import ca.metricalsky.winston.api.model.FetchLimitsResponse;
import ca.metricalsky.winston.service.fetch.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FetchController implements FetchApi {

    private final FetchService fetchService;

    @Override
    public ResponseEntity<FetchLimitsResponse> getFetchLimits() {
        var response = new FetchLimitsResponse()
                .dailyQuota(fetchService.getDailyQuota())
                .availableQuota(fetchService.getAvailableQuota());

        return ResponseEntity.ok(response);
    }
}

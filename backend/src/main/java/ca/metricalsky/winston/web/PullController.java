package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.PullApi;
import ca.metricalsky.winston.api.model.PullRequest;
import ca.metricalsky.winston.api.model.QuotePullRequest;
import ca.metricalsky.winston.api.model.QuotePullResponse;
import ca.metricalsky.winston.dao.PullRequestDataService;
import ca.metricalsky.winston.service.NotificationsService;
import ca.metricalsky.winston.service.PullRequestService;
import ca.metricalsky.winston.service.fetch.FetchService;
import ca.metricalsky.winston.service.pull.estimate.QuotaCostEstimationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class PullController implements PullApi {

    private final FetchService fetchService;
    private final NotificationsService notificationsService;
    private final PullRequestDataService pullRequestDataService;
    private final PullRequestService pullRequestService;
    private final QuotaCostEstimationService quotaCostEstimationService;

    @Override
    public ResponseEntity<Void> pull(PullRequest pullRequest) {
        pullRequestService.validateEstimatedQuotaCost(pullRequest);

        var ssePublisher = notificationsService.requireSubscription(pullRequest.getEventSubscriptionId());
        var fetchRequestId = pullRequestDataService.savePullRequest(pullRequest);
        fetchService.fetchAsync(fetchRequestId, ssePublisher);

        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<QuotePullResponse> quotePull(QuotePullRequest estimateCostRequest) {
        var operations = estimateCostRequest.getOperations();
        var totalEstimatedCost = quotaCostEstimationService.estimateQuotaCost(operations);

        return ResponseEntity.ok(new QuotePullResponse()
                .totalEstimatedCost(totalEstimatedCost)
                .operations(operations));
    }

    @PostMapping("/api/dev/pull")
    public ResponseEntity<SseEmitter> debugPull(@Valid @RequestBody PullRequest pullRequest) {
        pullRequestService.validateEstimatedQuotaCost(pullRequest);

        var ssePublisher = notificationsService.openSubscription();
        var fetchRequestId = pullRequestDataService.savePullRequest(pullRequest);
        fetchService.fetchAsync(fetchRequestId, ssePublisher);

        return ResponseEntity.ok(ssePublisher.getSseEmitter());
    }
}

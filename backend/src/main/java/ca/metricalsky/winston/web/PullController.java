package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.PullApi;
import ca.metricalsky.winston.api.model.PullRequest;
import ca.metricalsky.winston.dao.PullRequestDataService;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.mapper.entity.FetchRequestEntityMapper;
import ca.metricalsky.winston.repository.fetch.FetchRequestRepository;
import ca.metricalsky.winston.service.NotificationsService;
import ca.metricalsky.winston.service.fetch.FetchService;
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

    @Override
    public ResponseEntity<Void> pull(PullRequest pullRequest) {
        var ssePublisher = notificationsService.requireSubscription(pullRequest.getEventListenerId());
        var fetchRequestId = pullRequestDataService.savePullRequest(pullRequest);
        fetchService.fetchAsync(fetchRequestId, ssePublisher);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/api/dev/pull")
    public ResponseEntity<SseEmitter> debugPull(@Valid @RequestBody PullRequest pullRequest) {
        var ssePublisher = notificationsService.openSubscription();
        var fetchRequestId = pullRequestDataService.savePullRequest(pullRequest);
        fetchService.fetchAsync(fetchRequestId, ssePublisher);

        return ResponseEntity.ok(ssePublisher.getSseEmitter());
    }
}

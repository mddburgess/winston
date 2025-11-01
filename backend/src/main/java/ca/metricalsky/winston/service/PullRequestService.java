package ca.metricalsky.winston.service;

import ca.metricalsky.winston.api.model.PullRequest;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.service.fetch.FetchService;
import ca.metricalsky.winston.service.pull.estimate.QuotaCostEstimationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PullRequestService {

    private final FetchService fetchService;
    private final QuotaCostEstimationService quotaCostEstimationService;

    public void validateEstimatedQuotaCost(PullRequest pullRequest) {
        var estimatedCost = quotaCostEstimationService.estimateQuotaCost(pullRequest.getOperations());
        var availableQuota = fetchService.getAvailableQuota();

        if (estimatedCost > availableQuota) {
            throw new AppException(ErrorCode.REQUEST_TOO_EXPENSIVE);
        }
    }
}

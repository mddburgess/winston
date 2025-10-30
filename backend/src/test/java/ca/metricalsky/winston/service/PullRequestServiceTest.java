package ca.metricalsky.winston.service;

import ca.metricalsky.winston.api.model.PullCommentsOperation;
import ca.metricalsky.winston.api.model.PullRequest;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.service.fetch.FetchService;
import ca.metricalsky.winston.service.pull.estimate.QuotaCostEstimationService;
import ca.metricalsky.winston.test.annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@UnitTest
class PullRequestServiceTest {

    @InjectMocks
    private PullRequestService pullRequestService;

    @Mock
    private FetchService fetchService;
    @Mock
    private QuotaCostEstimationService  quotaCostEstimationService;

    private final PullRequest pullRequest = new PullRequest()
            .addOperationsItem(new PullCommentsOperation());

    @Test
    void validateEstimatedQuotaCost_estimateLessThanAvailable() {
        when(quotaCostEstimationService.estimateQuotaCost(pullRequest.getOperations()))
                .thenReturn(100);
        when(fetchService.getAvailableQuota())
                .thenReturn(1000);

        assertThatCode(() -> pullRequestService.validateEstimatedQuotaCost(pullRequest))
                .doesNotThrowAnyException();
    }

    @Test
    void validateEstimatedQuotaCost_estimateEqualToAvailable() {
        when(quotaCostEstimationService.estimateQuotaCost(pullRequest.getOperations()))
                .thenReturn(100);
        when(fetchService.getAvailableQuota())
                .thenReturn(100);

        assertThatCode(() -> pullRequestService.validateEstimatedQuotaCost(pullRequest))
                .doesNotThrowAnyException();
    }

    @Test
    void validateEstimatedQuotaCost_estimateGreaterThanAvailable() {
        when(quotaCostEstimationService.estimateQuotaCost(pullRequest.getOperations()))
                .thenReturn(100);
        when(fetchService.getAvailableQuota())
                .thenReturn(10);

        assertThatThrownBy(() -> pullRequestService.validateEstimatedQuotaCost(pullRequest))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.UNPROCESSABLE_ENTITY)
                .hasMessageEndingWith(ErrorCode.REQUEST_TOO_EXPENSIVE.getDetail());
    }
}

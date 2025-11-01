package ca.metricalsky.winston.service.pull.estimate;

import ca.metricalsky.winston.api.model.PullChannelOperation;
import ca.metricalsky.winston.api.model.PullCommentsOperation;
import ca.metricalsky.winston.api.model.PullOperation;
import ca.metricalsky.winston.api.model.PullRepliesOperation;
import ca.metricalsky.winston.api.model.PullVideosOperation;
import ca.metricalsky.winston.test.annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@UnitTest
class QuotaCostEstimationServiceTest {

    @InjectMocks
    private QuotaCostEstimationService quotaCostEstimationService;

    @Mock
    private ChannelQuotaCostEstimator channelQuotaCostEstimator;
    @Mock
    private CommentsQuotaCostEstimator commentsQuotaCostEstimator;
    @Mock
    private RepliesQuotaCostEstimator repliesQuotaCostEstimator;
    @Mock
    private VideosQuotaCostEstimator videosQuotaCostEstimator;

    @Test
    void estimateQuotaCost() {
        var pullOperations = List.of(
                new PullChannelOperation(),
                new PullVideosOperation(),
                new PullCommentsOperation(),
                new PullRepliesOperation()
        );

        when(channelQuotaCostEstimator.estimateQuotaCost(any()))
                .thenReturn(1);
        when(videosQuotaCostEstimator.estimateQuotaCost(any()))
                .thenReturn(2);
        when(commentsQuotaCostEstimator.estimateQuotaCost(any()))
                .thenReturn(4);
        when(repliesQuotaCostEstimator.estimateQuotaCost(any()))
                .thenReturn(8);

        var result =  quotaCostEstimationService.estimateQuotaCost(pullOperations);

        assertThat(result)
                .isEqualTo(15);
    }

    @Test
    void estimateQuotaCost_throwsException() {
        var pullOperations = new ArrayList<PullOperation>();
        pullOperations.add(null);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> quotaCostEstimationService.estimateQuotaCost(pullOperations));
    }
}

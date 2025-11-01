package ca.metricalsky.winston.service.pull.estimate;

import ca.metricalsky.winston.api.model.PullChannelOperation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChannelQuotaCostEstimatorTest {

    private final ChannelQuotaCostEstimator estimator = new ChannelQuotaCostEstimator();

    @Test
    void estimateQuotaCost() {
        var operation = new PullChannelOperation();

        var estimate = estimator.estimateQuotaCost(operation);

        assertThat(estimate)
                .isEqualTo(1);
        assertThat(operation.getEstimatedCost())
                .isEqualTo(estimate);
    }
}

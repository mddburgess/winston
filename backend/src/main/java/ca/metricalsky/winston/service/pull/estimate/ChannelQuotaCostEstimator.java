package ca.metricalsky.winston.service.pull.estimate;

import ca.metricalsky.winston.api.model.PullChannelOperation;
import org.springframework.stereotype.Service;

@Service
public class ChannelQuotaCostEstimator implements QuotaCostEstimator<PullChannelOperation> {

    @Override
    public int estimateQuotaCost(PullChannelOperation operation) {
        return 1;
    }
}

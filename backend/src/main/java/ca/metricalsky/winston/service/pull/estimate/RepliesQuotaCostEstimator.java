package ca.metricalsky.winston.service.pull.estimate;

import ca.metricalsky.winston.api.model.PullRepliesOperation;
import org.springframework.stereotype.Service;

@Service
public class RepliesQuotaCostEstimator implements QuotaCostEstimator<PullRepliesOperation> {

    @Override
    public int estimateQuotaCost(PullRepliesOperation operation) {
        operation.setEstimatedCost(1);
        return operation.getEstimatedCost();
    }
}

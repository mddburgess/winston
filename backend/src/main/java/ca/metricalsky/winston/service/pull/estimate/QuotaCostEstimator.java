package ca.metricalsky.winston.service.pull.estimate;

public interface QuotaCostEstimator<T> {

    int estimateQuotaCost(T operation);
}

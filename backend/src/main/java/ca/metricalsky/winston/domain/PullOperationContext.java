package ca.metricalsky.winston.domain;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PullOperationContext {

    private FetchOperationEntity operation;
    private FetchActionEntity nextAction;
    private int totalCount;
    private List results;

    public void setResults(List results) {
        this.results = results;
        this.totalCount += results != null ? results.size() : 0;
    }

    public boolean hasNextAction() {
        return nextAction != null;
    }
}

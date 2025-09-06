package ca.metricalsky.winston.events.model;

import ca.metricalsky.winston.api.model.PullOperation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collection;

@JsonPropertyOrder({
        "event_id",
        "event_type",
        "operation",
        "results"
})
public class PullResultsEvent<T> extends AppEvent {

    private static final String EVENT_TYPE = "pull-results";

    @JsonProperty("operation")
    private final PullOperation operation;

    @JsonProperty("results")
    private final Results<T> results;

    public PullResultsEvent(PullOperation operation, int totalCount, Collection<T> items) {
        super(EVENT_TYPE);
        this.operation = operation;
        this.results = new Results<>(totalCount, items);
    }
}

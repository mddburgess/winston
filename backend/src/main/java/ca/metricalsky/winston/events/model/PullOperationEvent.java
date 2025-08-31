package ca.metricalsky.winston.events.model;

import ca.metricalsky.winston.api.model.PullOperation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "event_id",
        "event_type",
        "operation",
        "error"
})
public class PullOperationEvent extends AppEvent {

    private static final String EVENT_TYPE = "pull-operation";

    @JsonProperty("operation")
    private final PullOperation operation;

    public PullOperationEvent(PullOperation operation) {
        super(EVENT_TYPE);
        this.operation = operation;
    }
}

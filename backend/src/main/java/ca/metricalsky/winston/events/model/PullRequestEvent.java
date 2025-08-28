package ca.metricalsky.winston.events.model;

import ca.metricalsky.winston.api.model.PullRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "event_id",
        "event_type",
        "operations",
        "error"
})
public class PullRequestEvent extends AppEvent {

    private static final String EVENT_TYPE = "pull-request";

    @JsonProperty("request")
    private PullRequest request;

    public PullRequestEvent(PullRequest request) {
        super(EVENT_TYPE);
        this.request = request;
    }
}

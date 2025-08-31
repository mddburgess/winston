package ca.metricalsky.winston.events.model;

import ca.metricalsky.winston.api.model.Problem;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.UUID;

@JsonPropertyOrder({
        "event_id",
        "event_type",
        "event_subscription_id",
        "subscribed",
        "error"
})
public class EventSubscriptionEvent extends AppEvent {

    private static final String EVENT_TYPE = "event-subscription";

    @JsonProperty("event_subscription_id")
    private final UUID eventSubscriptionId;

    @JsonProperty("subscribed")
    private final boolean subscribed;

    @JsonProperty("problem")
    private final Problem problem;

    public EventSubscriptionEvent(UUID eventSubscriptionId, boolean subscribed) {
        super(EVENT_TYPE);
        this.eventSubscriptionId = eventSubscriptionId;
        this.subscribed = subscribed;
        this.problem = null;
    }

    public EventSubscriptionEvent(UUID eventSubscriptionId, Problem problem) {
        super(EVENT_TYPE);
        this.eventSubscriptionId = eventSubscriptionId;
        this.subscribed = false;
        this.problem = problem;
    }
}

package ca.metricalsky.winston.events.model;

import ca.metricalsky.winston.api.model.Channel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "event_id",
        "event_type",
        "channels"
})
public class PullChannelsEvent extends AppEvent {

    private static final String EVENT_TYPE = "pull-channels";

    @JsonProperty("channels")
    private final List<Channel> channels;

    public PullChannelsEvent(List<Channel> channels) {
        super(EVENT_TYPE);
        this.channels = channels;
    }
}

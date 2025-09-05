package ca.metricalsky.winston.events.model;

import ca.metricalsky.winston.api.model.Video;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "event_id",
        "event_type",
        "channel_handle",
        "videos"
})
public class PullVideosEvent extends AppEvent {

    private static final String EVENT_TYPE = "pull-videos";

    @JsonProperty("channel_handle")
    private final String channelHandle;

    @JsonProperty("videos")
    private final List<Video> videos;

    public PullVideosEvent(String channelHandle, List<Video> videos) {
        super(EVENT_TYPE);
        this.channelHandle = channelHandle;
        this.videos = videos;
    }
}

package ca.metricalsky.winston.events;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.api.model.Video;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PullDataEvent extends AppEvent {

    private static final String EVENT_TYPE = "pull-data";

    private String objectId;
    private Channel channel;
    private List<Video> videos;
    private List<TopLevelComment> comments;
    private List<Comment> replies;

    public PullDataEvent() {
        super(EVENT_TYPE);
    }
}

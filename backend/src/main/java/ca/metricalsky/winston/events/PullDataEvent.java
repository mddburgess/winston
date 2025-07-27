package ca.metricalsky.winston.events;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.api.model.Video;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PullDataEvent {

    private String objectId;
    private Channel channel;
    private List<Video> videos;
    private List<TopLevelComment> comments;
    private List<Comment> replies;
}

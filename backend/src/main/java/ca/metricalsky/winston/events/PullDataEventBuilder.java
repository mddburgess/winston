package ca.metricalsky.winston.events;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.service.fetch.FetchResult;

import java.util.List;

public class PullDataEventBuilder implements EventBuilder {

    @Override
    public Object buildEvent(FetchResult fetchResult) {
        var pullDataEvent = new PullDataEvent();
//        pullDataEvent.setObjectId(fetchResult.objectId());

        var items = fetchResult.items();
        if (!items.isEmpty()) {
            switch (items.getFirst()) {
                case Channel channel -> pullDataEvent.setChannel(channel);
                case Video _ -> pullDataEvent.setVideos((List<Video>) fetchResult.items());
                case TopLevelComment _ -> pullDataEvent.setComments((List<TopLevelComment>) fetchResult.items());
                case Comment _ -> pullDataEvent.setReplies((List<Comment>) fetchResult.items());
                default -> {}
            };
        }

        return pullDataEvent;
    }
}

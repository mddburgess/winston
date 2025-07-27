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
        var pullDataEvent = PullDataEvent.builder()
                .objectId(fetchResult.objectId());

        var items = fetchResult.items();
        if (!items.isEmpty()) {
            pullDataEvent = switch (items.getFirst()) {
                case Channel channel -> pullDataEvent.channel(channel);
                case Video _ -> pullDataEvent.videos((List<Video>) fetchResult.items());
                case TopLevelComment _ -> pullDataEvent.comments((List<TopLevelComment>) fetchResult.items());
                case Comment _ -> pullDataEvent.replies((List<Comment>) fetchResult.items());
                default -> pullDataEvent;
            };
        }

        return pullDataEvent.build();
    }
}

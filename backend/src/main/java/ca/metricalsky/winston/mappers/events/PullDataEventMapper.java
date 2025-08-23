package ca.metricalsky.winston.mappers.events;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.events.AppEvent;
import ca.metricalsky.winston.events.PullDataEvent;
import ca.metricalsky.winston.service.fetch.FetchResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PullDataEventMapper implements Converter<FetchResult, AppEvent> {

    @Override
    public AppEvent convert(FetchResult source) {
        var pullDataEvent = new PullDataEvent();
        pullDataEvent.setObjectId(source.objectId());

        var items = source.items();
        if (!items.isEmpty()) {
            switch (items.getFirst()) {
                case Channel channel -> pullDataEvent.setChannel(channel);
                case Video _ -> pullDataEvent.setVideos((List<Video>) source.items());
                case TopLevelComment _ -> pullDataEvent.setComments((List<TopLevelComment>) source.items());
                case Comment _ -> pullDataEvent.setReplies((List<Comment>) source.items());
                default -> {}
            };
        }

        return pullDataEvent;
    }
}

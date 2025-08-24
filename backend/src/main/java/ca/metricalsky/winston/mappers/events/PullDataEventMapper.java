package ca.metricalsky.winston.mappers.events;

import ca.metricalsky.winston.api.model.AppEvent;
import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.service.fetch.FetchResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PullDataEventMapper implements Converter<FetchResult, AppEvent> {

    private static final String EVENT_TYPE = "pull-data";

    @Override
    public AppEvent convert(FetchResult source) {
        var appEvent = new AppEvent();
        appEvent.setEventId(UUID.randomUUID());
        appEvent.setEventType(EVENT_TYPE);

        var items = source.items();
        if (!items.isEmpty()) {
            switch (items.getFirst()) {
                case Channel channel -> appEvent.setChannel(channel);
                case Video _ -> appEvent.setVideos((List<Video>) source.items());
                case TopLevelComment _ -> appEvent.setComments((List<TopLevelComment>) source.items());
                case Comment _ -> appEvent.setReplies((List<Comment>) source.items());
                default -> {}
            };
        }

        return appEvent;
    }
}

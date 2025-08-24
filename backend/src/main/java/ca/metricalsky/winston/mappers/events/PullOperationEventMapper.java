package ca.metricalsky.winston.mappers.events;

import ca.metricalsky.winston.api.model.AppEvent;
import ca.metricalsky.winston.api.model.PullChannelOperation;
import ca.metricalsky.winston.api.model.PullCommentsOperation;
import ca.metricalsky.winston.api.model.PullOperation;
import ca.metricalsky.winston.api.model.PullRepliesOperation;
import ca.metricalsky.winston.api.model.PullVideosOperation;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class PullOperationEventMapper implements Converter<FetchOperationEntity, AppEvent> {

    private static final String EVENT_TYPE = "pull-operation";

    @Override
    public AppEvent convert(FetchOperationEntity source) {
        var appEvent = new AppEvent();
        appEvent.setEventId(UUID.randomUUID());
        appEvent.setEventType(EVENT_TYPE);

        appEvent.setOperation(convertFetchOperation(source));

        return appEvent;
    }

    private PullOperation convertFetchOperation(FetchOperationEntity source) {
        return switch (source.getOperationType()) {
            case CHANNELS -> toPullChannel(source);
            case VIDEOS -> toPullVideos(source);
            case COMMENTS -> toPullComments(source);
            case REPLIES -> toPullReplies(source);
        };
    }

    @Mapping(target = "pull", constant = "CHANNEL")
    @Mapping(target = "channelHandle", source = "objectId")
    protected abstract PullChannelOperation toPullChannel(FetchOperationEntity source);

    @Mapping(target = "pull", constant = "VIDEOS")
    @Mapping(target = "channelHandle", source = "objectId")
    @Mapping(target = "range", source = "mode")
    protected abstract PullVideosOperation toPullVideos(FetchOperationEntity source);

    @Mapping(target = "pull", constant = "COMMENTS")
    @Mapping(target = "videoId", source = "objectId")
    protected abstract PullCommentsOperation toPullComments(FetchOperationEntity source);

    @Mapping(target = "pull", constant = "REPLIES")
    @Mapping(target = "videoId", source = ".", qualifiedByName = "getPullRepliesVideoId")
    @Mapping(target = "commentId", source = ".", qualifiedByName = "getPullRepliesCommentId")
    protected abstract PullRepliesOperation toPullReplies(FetchOperationEntity source);

    @Named("getPullRepliesVideoId")
    protected String getPullRepliesVideoId(FetchOperationEntity source) {
        return "FOR_VIDEO".equals(source.getMode()) ? source.getObjectId() : null;
    }

    @Named("getPullRepliesCommentId")
    protected String getPullRepliesCommentId(FetchOperationEntity source) {
        return "FOR_COMMENT".equals(source.getMode()) ? source.getObjectId() : null;
    }
}

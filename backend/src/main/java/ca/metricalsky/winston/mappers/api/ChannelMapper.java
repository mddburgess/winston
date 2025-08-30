package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.entity.ChannelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.net.URI;

@Mapper(componentModel = "spring")
public abstract class ChannelMapper {

    @Mapping(target = "handle", source = "customUrl")
    @Mapping(target = "thumbnailUrl", source = ".")
    @Mapping(target = "statistics.videoCount", source = "videoCount", defaultValue = "0L")
    @Mapping(target = "statistics.viewCount", source = "viewCount", defaultValue = "0L")
    @Mapping(target = "statistics.subscriberCount", source = "subscriberCount", defaultValue = "0L")
    @Mapping(target = "videoCount", ignore = true)
    public abstract Channel toChannel(ChannelEntity channelEntity);

    protected String getThumbnailUrl(ChannelEntity channelEntity) {
        return channelEntity.getThumbnailUrl() != null
                ? "/api/v1/channels/" + channelEntity.getId() + "/thumbnail"
                : null;
    }

    protected URI toURI(String uriString) {
        return URI.create(uriString);
    }
}

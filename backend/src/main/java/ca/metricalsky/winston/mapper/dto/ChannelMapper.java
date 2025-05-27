package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.entity.ChannelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.net.URI;

@Mapper(componentModel = "spring")
public abstract class ChannelMapper {

    @Mapping(target = "handle", source = "customUrl")
    @Mapping(target = "thumbnailUrl", source = ".")
    @Mapping(target = "videoCount", ignore = true)
    public abstract Channel toChannel(ChannelEntity channelEntity);

    protected String getThumbnailUrl(ChannelEntity channelEntity) {
        return channelEntity.getThumbnailUrl() != null
                ? "/api/v1/channels/" + channelEntity.getId() + "/thumbnail"
                : "";
    }

    protected URI toURI(String uriString) {
        return URI.create(uriString);
    }
}

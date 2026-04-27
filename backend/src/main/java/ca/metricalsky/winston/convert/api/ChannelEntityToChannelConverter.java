package ca.metricalsky.winston.convert.api;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.ChannelProperties;
import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.database.entity.channel.ChannelPropertiesEntity;
import ca.metricalsky.winston.entity.ChannelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring", uses = ConversionServiceAdapter.class)
public abstract class ChannelEntityToChannelConverter
        implements Converter<ChannelEntity, Channel> {

    @Override
    @Mapping(target = "handle", source = "customUrl")
    @Mapping(target = "thumbnailUrl", source = ".")
    @Mapping(target = "statistics.videoCount", source = "videoCount", defaultValue = "0L")
    @Mapping(target = "statistics.viewCount", source = "viewCount", defaultValue = "0L")
    @Mapping(target = "statistics.subscriberCount", source = "subscriberCount", defaultValue = "0L")
    @Mapping(target = "videoCount", ignore = true)
    public abstract Channel convert(ChannelEntity source);

    String getThumbnailUrl(ChannelEntity channelEntity) {
        return channelEntity.getThumbnailUrl() != null
                ? "/api/v1/channels/" + channelEntity.getId() + "/thumbnail"
                : null;
    }

    ChannelProperties getProperties(ChannelPropertiesEntity channelPropertiesEntity) {
        return new ChannelProperties()
                .archived(channelPropertiesEntity != null && channelPropertiesEntity.getArchived());
    }
}

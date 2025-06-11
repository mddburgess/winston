package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.dto.ChannelDto;
import ca.metricalsky.winston.entity.ChannelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.lang.NonNull;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Deprecated(since = "1.4.0", forRemoval = true)
@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public abstract class ChannelDtoMapper {

    public ChannelDto fromEntity(ChannelEntity channel) {
        if (channel == null) {
            return null;
        }

        var channelDto = new ChannelDto();
        mapToChannelDto(channel, channelDto);
        return channelDto;
    }

    @Mapping(target = "thumbnailUrl", source = ".")
    @Mapping(target = "videoCount", ignore = true)
    abstract void mapToChannelDto(ChannelEntity channel, @MappingTarget ChannelDto channelDto);

    @NonNull
    String getThumbnailUrl(ChannelEntity channel) {
        return channel.getThumbnailUrl() != null
                ? "/api/v1/channels/" + channel.getId() + "/thumbnail"
                : "";
    }
}

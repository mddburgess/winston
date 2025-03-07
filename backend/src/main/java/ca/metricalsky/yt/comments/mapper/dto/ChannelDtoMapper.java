package ca.metricalsky.yt.comments.mapper.dto;

import ca.metricalsky.yt.comments.dto.ChannelDto;
import ca.metricalsky.yt.comments.entity.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.lang.NonNull;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public abstract class ChannelDtoMapper {

    public ChannelDto fromEntity(Channel channel) {
        if (channel == null) {
            return null;
        }

        var channelDto = new ChannelDto();
        mapToChannelDto(channel, channelDto);
        return channelDto;
    }

    @Mapping(target = "thumbnailUrl", source = ".")
    @Mapping(target = "videoCount", ignore = true)
    abstract void mapToChannelDto(Channel channel, @MappingTarget ChannelDto channelDto);

    @NonNull
    String getThumbnailUrl(Channel channel) {
        return channel.getThumbnailUrl() != null
                ? "/api/channels/" + channel.getId() + "/thumbnail"
                : "";
    }
}

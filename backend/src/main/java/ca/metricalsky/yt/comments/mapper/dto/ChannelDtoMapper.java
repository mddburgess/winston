package ca.metricalsky.yt.comments.mapper.dto;

import ca.metricalsky.yt.comments.dto.ChannelDto;
import ca.metricalsky.yt.comments.entity.Channel;
import ca.metricalsky.yt.comments.mapper.OffsetDateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class ChannelDtoMapper {

    @Mapping(target = "thumbnailUrl", source = ".")
    @Mapping(target = "videoCount", ignore = true)
    public abstract ChannelDto fromEntity(Channel channel);

    String getThumbnailUrl(Channel channel) {
        return "/api/channels/" + channel.getId() + "/thumbnail";
    }
}

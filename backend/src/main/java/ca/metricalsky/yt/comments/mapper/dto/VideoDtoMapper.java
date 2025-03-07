package ca.metricalsky.yt.comments.mapper.dto;

import ca.metricalsky.yt.comments.dto.VideoDto;
import ca.metricalsky.yt.comments.entity.Video;
import ca.metricalsky.yt.comments.mapper.OffsetDateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class VideoDtoMapper {

    @Mapping(target = "thumbnailUrl", source = ".")
    @Mapping(target = "channel", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "replyCount", ignore = true)
    @Mapping(target = "totalReplyCount", ignore = true)
    public abstract VideoDto fromEntity(Video video);

    String getThumbnailUrl(Video video) {
        return "/api/videos/" + video.getId() + "/thumbnail";
    }
}

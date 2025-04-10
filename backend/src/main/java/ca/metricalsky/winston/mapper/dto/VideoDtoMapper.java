package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.entity.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public abstract class VideoDtoMapper {

    public VideoDto fromEntity(Video video) {
        if (video == null) {
            return null;
        }

        var videoDto = new VideoDto();
        mapToVideoDto(video, videoDto);
        return videoDto;
    }

    @Mapping(target = "thumbnailUrl", source = ".")
    @Mapping(target = "channel", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "replyCount", ignore = true)
    @Mapping(target = "totalReplyCount", ignore = true)
    abstract void mapToVideoDto(Video video, @MappingTarget VideoDto videoDto);

    String getThumbnailUrl(Video video) {
        return video.getThumbnailUrl() != null
                ? "/api/videos/" + video.getId() + "/thumbnail"
                : "";
    }
}

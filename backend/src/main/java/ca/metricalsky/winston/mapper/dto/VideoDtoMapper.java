package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.entity.VideoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Deprecated(since = "1.4.0", forRemoval = true)
@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public abstract class VideoDtoMapper {

    public VideoDto fromEntity(VideoEntity video) {
        if (video == null) {
            return null;
        }

        var videoDto = new VideoDto();
        mapToVideoDto(video, videoDto);
        return videoDto;
    }

    @Mapping(target = "thumbnailUrl", source = ".")
    abstract void mapToVideoDto(VideoEntity video, @MappingTarget VideoDto videoDto);

    String getThumbnailUrl(VideoEntity video) {
        return video.getThumbnailUrl() != null
                ? "/api/v1/videos/" + video.getId() + "/thumbnail"
                : "";
    }
}

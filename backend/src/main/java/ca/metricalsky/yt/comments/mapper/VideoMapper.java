package ca.metricalsky.yt.comments.mapper;

import ca.metricalsky.yt.comments.dto.VideoDto;
import ca.metricalsky.yt.comments.entity.Video;
import com.google.api.services.youtube.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class VideoMapper {

    @Mapping(target = "id", source = "contentDetails.upload.videoId")
    @Mapping(target = "channelId", source = "snippet.channelId")
    @Mapping(target = "title", source = "snippet.title")
    @Mapping(target = "description", source = "snippet.description")
    @Mapping(target = "thumbnailUrl", source = "snippet.thumbnails.high.url")
    @Mapping(target = "publishedAt", source = "snippet.publishedAt")
    @Mapping(target = "lastFetchedAt", ignore = true)
    public abstract Video fromYouTube(Activity activity);

    @Mapping(target = "thumbnailUrl", source = ".")
    @Mapping(target = "channel", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "replyCount", ignore = true)
    @Mapping(target = "totalReplyCount", ignore = true)
    public abstract VideoDto toDto(Video video);

    String toDtoThumbnailUrl(Video video) {
        return "/api/videos/" + video.getId() + "/thumbnail";
    }
}

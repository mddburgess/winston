package ca.metricalsky.yt.comments.mapper.entity;

import ca.metricalsky.yt.comments.entity.Video;
import ca.metricalsky.yt.comments.mapper.OffsetDateTimeMapper;
import com.google.api.services.youtube.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface VideoMapper {

    @Mapping(target = "id", source = "contentDetails.upload.videoId")
    @Mapping(target = "channelId", source = "snippet.channelId")
    @Mapping(target = "title", source = "snippet.title")
    @Mapping(target = "description", source = "snippet.description")
    @Mapping(target = "thumbnailUrl", source = "snippet.thumbnails.high.url")
    @Mapping(target = "publishedAt", source = "snippet.publishedAt")
    @Mapping(target = "lastFetchedAt", ignore = true)
    Video fromYouTube(Activity activity);
}

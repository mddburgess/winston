package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.entity.VideoEntity;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.PlaylistItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface VideoEntityMapper {

    @Mapping(target = "id", source = "contentDetails.upload.videoId")
    @Mapping(target = "channelId", source = "snippet.channelId")
    @Mapping(target = "title", source = "snippet.title")
    @Mapping(target = "description", source = "snippet.description")
    @Mapping(target = "thumbnailUrl", source = "snippet.thumbnails.high.url")
    @Mapping(target = "publishedAt", source = "snippet.publishedAt")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastFetchedAt", ignore = true)
    VideoEntity toVideoEntity(Activity activity);

    @Mapping(target = "id", source = "contentDetails.videoId")
    @Mapping(target = "channelId", source = "snippet.channelId")
    @Mapping(target = "title", source = "snippet.title")
    @Mapping(target = "description", source = "snippet.description")
    @Mapping(target = "thumbnailUrl", source = "snippet.thumbnails.high.url")
    @Mapping(target = "publishedAt", source = "snippet.publishedAt")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastFetchedAt", ignore = true)
    VideoEntity toVideoEntity(PlaylistItem playlistItem);
}

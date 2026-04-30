package ca.metricalsky.winston.convert.entity;

import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.database.entity.video.VideoEntity;
import com.google.api.services.youtube.model.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public interface YoutubeVideoToVideoEntityConverter
        extends Converter<Video, VideoEntity> {

    @Override
    @Mapping(target = "id", source = "id")
    @Mapping(target = "channelId", source = "snippet.channelId")
    @Mapping(target = "title", source = "snippet.title")
    @Mapping(target = "description", source = "snippet.description")
    @Mapping(target = "thumbnailUrl", source = "snippet.thumbnails.high.url")
    @Mapping(target = "publishedAt", source = "snippet.publishedAt")
    @Mapping(target = "details", source = ".")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastFetchedAt", ignore = true)
    VideoEntity convert(Video source);
}

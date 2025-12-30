package ca.metricalsky.winston.convert.api;

import ca.metricalsky.winston.api.model.AuthorVideo;
import ca.metricalsky.winston.entity.view.AuthorVideoView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface AuthorVideoViewToAuthorVideoConverter
        extends Converter<AuthorVideoView, AuthorVideo> {

    @Override
    @Mapping(target = "id", source = "videoId")
    @Mapping(target = "title", source = "videoTitle")
    @Mapping(target = "thumbnailUrl", source = "videoThumbnailUrl")
    AuthorVideo convert(AuthorVideoView source);
}

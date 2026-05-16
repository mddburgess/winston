package ca.metricalsky.winston.convert.api;

import ca.metricalsky.winston.api.model.AuthorChannel;
import ca.metricalsky.winston.database.view.AuthorChannelView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface AuthorChannelViewToAuthorChannelConverter
        extends Converter<AuthorChannelView, AuthorChannel> {

    @Override
    @Mapping(target = "title", source = "channelTitle")
    @Mapping(target = "handle", source = "channelHandle")
    AuthorChannel convert(AuthorChannelView source);
}

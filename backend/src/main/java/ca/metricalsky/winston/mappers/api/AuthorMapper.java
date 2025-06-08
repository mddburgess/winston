package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.view.AuthorDetailsView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper {

    @Mapping(target = "handle", source = ".", qualifiedByName = "mapHandle")
    @Mapping(target = "profileImageUrl", source = ".", qualifiedByName = "mapProfileImageUrl")
    @Mapping(target = "statistics", ignore = true)
    public abstract Author toAuthor(AuthorEntity authorEntity);

    @Mapping(target = ".", source = "author")
    @Mapping(target = "handle", source = "author", qualifiedByName = "mapHandle")
    @Mapping(target = "profileImageUrl", source = "author", qualifiedByName = "mapProfileImageUrl")
    @Mapping(target = "statistics", source = ".")
    public abstract Author toAuthor(AuthorDetailsView authorDetailsView);

    @Named("mapHandle")
    protected String mapHandle(AuthorEntity author) {
        if (isNotBlank(author.getDisplayName())) {
            return author.getDisplayName();
        }
        if (isNotBlank(author.getChannelUrl())) {
            var channelUrlParts = author.getChannelUrl().split("/c/");
            if (channelUrlParts.length > 1) {
                return URLDecoder.decode(channelUrlParts[1], StandardCharsets.UTF_8);
            }
        }
        return defaultIfBlank(author.getId(), null);
    }

    @Named("mapProfileImageUrl")
    protected String mapProfileImageUrl(AuthorEntity author) {
        return author.getProfileImageUrl() != null
                ? "/api/v1/authors/" + author.getId() + "/thumbnail"
                : null;
    }
}

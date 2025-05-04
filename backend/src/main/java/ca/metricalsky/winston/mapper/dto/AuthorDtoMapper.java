package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.dto.author.AuthorDto;
import ca.metricalsky.winston.entity.Author;
import ca.metricalsky.winston.entity.view.AuthorDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public abstract class AuthorDtoMapper {

    public AuthorDto toAuthorDto(Author author) {
        if (author == null) {
            return null;
        }

        var authorDto = new AuthorDto();
        mapToAuthorDto(author, authorDto);
        return authorDto;
    }

    @Mapping(target = ".", source = "author")
    @Mapping(target = "displayName", source = "author", qualifiedByName = "mapDisplayName")
    @Mapping(target = "profileImageUrl", source = "author", qualifiedByName = "mapProfileImageUrl")
    @Mapping(target = "statistics", source = ".")
    public abstract AuthorDto toAuthorDto(AuthorDetails authorDetails);

    @Mapping(target = "displayName", source = ".", qualifiedByName = "mapDisplayName")
    @Mapping(target = "profileImageUrl", source = ".", qualifiedByName = "mapProfileImageUrl")
    @Mapping(target = "statistics", ignore = true)
    abstract void mapToAuthorDto(Author author, @MappingTarget AuthorDto authorDto);

    @Named("mapDisplayName")
    protected String mapDisplayName(Author author) {
        if (isNotBlank(author.getDisplayName())) {
            return author.getDisplayName();
        }
        if (isNotBlank(author.getChannelUrl())) {
            var channelUrlParts = author.getChannelUrl().split("/c/");
            if (channelUrlParts.length > 1) {
                return URLDecoder.decode(channelUrlParts[1], StandardCharsets.UTF_8);
            }
        }
        return defaultIfBlank(author.getId(), "");
    }

    @Named("mapProfileImageUrl")
    protected String mapProfileImageUrl(Author author) {
        return author.getProfileImageUrl() != null
                ? "/api/v1/authors/" + author.getId() + "/thumbnail"
                : "";
    }
}

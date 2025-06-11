package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.dto.author.AuthorDto;
import ca.metricalsky.winston.entity.AuthorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Deprecated(since = "1.4.0", forRemoval = true)
@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public abstract class AuthorDtoMapper {

    @Mapping(target = "displayName", source = ".", qualifiedByName = "mapDisplayName")
    @Mapping(target = "profileImageUrl", source = ".", qualifiedByName = "mapProfileImageUrl")
    @Mapping(target = "statistics", ignore = true)
    abstract void mapToAuthorDto(AuthorEntity author, @MappingTarget AuthorDto authorDto);

    @Named("mapDisplayName")
    protected String mapDisplayName(AuthorEntity author) {
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
    protected String mapProfileImageUrl(AuthorEntity author) {
        return author.getProfileImageUrl() != null
                ? "/api/v1/authors/" + author.getId() + "/thumbnail"
                : "";
    }
}

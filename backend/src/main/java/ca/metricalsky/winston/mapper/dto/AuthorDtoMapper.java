package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.dto.author.AuthorDto;
import ca.metricalsky.winston.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    @Mapping(target = "displayName", source = ".")
    abstract void mapToAuthorDto(Author author, @MappingTarget AuthorDto authorDto);

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
}

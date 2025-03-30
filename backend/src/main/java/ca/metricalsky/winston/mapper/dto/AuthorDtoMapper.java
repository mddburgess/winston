package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.dto.AuthorDto;
import ca.metricalsky.winston.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

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

    abstract void mapToAuthorDto(Author author, @MappingTarget AuthorDto authorDto);
}

package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.entity.AuthorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper {

    @Mapping(target = "handle", source = "displayName")
    @Mapping(target = "statistics", ignore = true)
    public abstract Author toAuthor(AuthorEntity authorEntity);
}

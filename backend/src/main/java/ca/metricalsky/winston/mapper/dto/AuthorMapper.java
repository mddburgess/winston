package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.view.AuthorDetailsView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper {

    @Mapping(target = "handle", source = "displayName")
    @Mapping(target = "statistics", ignore = true)
    public abstract Author toAuthor(AuthorEntity authorEntity);

    @Mapping(target = ".", source = "author")
    @Mapping(target = "handle", source = "author.displayName")
    @Mapping(target = "statistics", source = ".")
    public abstract Author toAuthor(AuthorDetailsView authorDetailsView);
}

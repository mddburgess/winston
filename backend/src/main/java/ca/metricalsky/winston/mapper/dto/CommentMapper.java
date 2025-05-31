package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public abstract class CommentMapper {

    @Mapping(target = "text", source = "textDisplay")
    public abstract TopLevelComment toTopLevelComment(CommentEntity comment);

    @Mapping(target = "text", source = "textDisplay")
    public abstract Comment toComment(CommentEntity comment);
}

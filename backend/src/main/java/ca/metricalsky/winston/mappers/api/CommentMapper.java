package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public interface CommentMapper {

    @Mapping(target = "text", source = "textDisplay")
    @Mapping(target = "important", source = "properties.important", defaultValue = "false")
    @Mapping(target = "hidden", source = "properties.hidden", defaultValue = "false")
    TopLevelComment toTopLevelComment(CommentEntity comment);

    @Mapping(target = "text", source = "textDisplay")
    @Mapping(target = "important", source = "properties.important", defaultValue = "false")
    @Mapping(target = "hidden", source = "properties.hidden", defaultValue = "false")
    Comment toComment(CommentEntity comment);
}

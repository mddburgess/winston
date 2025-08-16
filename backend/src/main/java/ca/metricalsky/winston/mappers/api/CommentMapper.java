package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.CommentProperties;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.entity.CommentEntity;
import ca.metricalsky.winston.entity.CommentPropertiesEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public abstract class CommentMapper {

    @Mapping(target = "text.display", source = "textDisplay")
    @Mapping(target = "text.original", source = "textOriginal")
    public abstract TopLevelComment toTopLevelComment(CommentEntity comment);

    @InheritInverseConfiguration
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "properties", source = ".")
    @Mapping(target = "replies", ignore = true)
    public abstract CommentEntity toCommentEntity(TopLevelComment comment);

    @Mapping(target = "text.display", source = "textDisplay")
    @Mapping(target = "text.original", source = "textOriginal")
    public abstract Comment toComment(CommentEntity comment);

    CommentProperties toCommentProperties(CommentPropertiesEntity commentPropertiesEntity) {
        var commentProperties = new CommentProperties();
        commentProperties.setImportant(commentPropertiesEntity != null && commentPropertiesEntity.isImportant());
        commentProperties.setHidden(commentPropertiesEntity != null && commentPropertiesEntity.isHidden());
        return commentProperties;
    }

    @Mapping(target = "commentId", source = "id")
    @Mapping(target = "important", source = "properties.important")
    @Mapping(target = "hidden", source = "properties.hidden")
    abstract CommentPropertiesEntity toCommentPropertiesEntity(TopLevelComment comment);
}

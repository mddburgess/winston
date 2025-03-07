package ca.metricalsky.yt.comments.mapper.dto;

import ca.metricalsky.yt.comments.dto.CommentDto;
import ca.metricalsky.yt.comments.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(nullValuePropertyMappingStrategy = IGNORE)
public abstract class CommentDtoMapper {

    public CommentDto fromEntity(Comment comment) {
        if (comment == null) {
            return null;
        }

        var commentDto = new CommentDto();
        mapToCommentDto(comment, commentDto);
        return commentDto;
    }

    @Mapping(target = "text", source = "textDisplay")
    abstract void mapToCommentDto(Comment comment, @MappingTarget CommentDto commentDto);
}

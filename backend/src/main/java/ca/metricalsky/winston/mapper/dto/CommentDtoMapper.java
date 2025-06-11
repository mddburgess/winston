package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Deprecated(since = "1.4.0", forRemoval = true)
@Mapper(
        uses = {AuthorDtoMapper.class},
        nullValuePropertyMappingStrategy = IGNORE
)
public abstract class CommentDtoMapper {

    public CommentDto fromEntity(CommentEntity comment) {
        if (comment == null) {
            return null;
        }

        var commentDto = new CommentDto();
        if (comment.getParentId() != null) {
            mapReplyToCommentDto(comment, commentDto);
        } else {
            mapCommentToCommentDto(comment, commentDto);
        }
        return commentDto;
    }

    @Mapping(target = "text", source = "textDisplay")
    abstract void mapCommentToCommentDto(CommentEntity comment, @MappingTarget CommentDto commentDto);

    @Mapping(target = "text", source = "textDisplay")
    @Mapping(target = "replies", ignore = true)
    abstract void mapReplyToCommentDto(CommentEntity reply, @MappingTarget CommentDto replyDto);
}

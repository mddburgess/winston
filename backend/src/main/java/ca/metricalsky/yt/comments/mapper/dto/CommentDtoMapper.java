package ca.metricalsky.yt.comments.mapper.dto;

import ca.metricalsky.yt.comments.dto.CommentDto;
import ca.metricalsky.yt.comments.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentDtoMapper {

    @Mapping(target = "text", source = "textDisplay")
    CommentDto fromEntity(Comment comment);
}

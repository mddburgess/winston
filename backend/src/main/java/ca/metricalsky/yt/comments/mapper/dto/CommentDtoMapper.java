package ca.metricalsky.yt.comments.mapper.dto;

import ca.metricalsky.yt.comments.dto.CommentDto;
import ca.metricalsky.yt.comments.entity.Comment;
import ca.metricalsky.yt.comments.mapper.OffsetDateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface CommentDtoMapper {

    @Mapping(target = "text", source = "textDisplay")
    CommentDto fromEntity(Comment comment);
}

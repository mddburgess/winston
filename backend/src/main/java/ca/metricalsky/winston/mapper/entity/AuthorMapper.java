package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.entity.AuthorEntity;
import com.google.api.services.youtube.model.CommentSnippet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuthorMapper {

    @Mapping(target = "id", source = "authorChannelId.value")
    @Mapping(target = "displayName", source = "authorDisplayName")
    @Mapping(target = "channelUrl", source = "authorChannelUrl")
    @Mapping(target = "profileImageUrl", source = "authorProfileImageUrl")
    @Mapping(target = "lastFetchedAt", ignore = true)
    AuthorEntity getAuthor(CommentSnippet commentSnippet);
}

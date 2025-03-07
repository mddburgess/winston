package ca.metricalsky.yt.comments.mapper.entity;

import ca.metricalsky.yt.comments.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuthorMapper {

    @Mapping(target = "id", source = "authorChannelId.value")
    @Mapping(target = "displayName", source = "authorDisplayName")
    @Mapping(target = "channelUrl", source = "authorChannelUrl")
    @Mapping(target = "profileImageUrl", source = "authorProfileImageUrl")
    @Mapping(target = "lastFetchedAt", ignore = true)
    Author getAuthor(com.google.api.services.youtube.model.CommentSnippet commentSnippet);
}

package ca.metricalsky.yt.comments.mapper;

import ca.metricalsky.yt.comments.entity.Author;
import ca.metricalsky.yt.comments.entity.Comment;
import com.google.api.services.youtube.model.CommentThread;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface CommentMapper {

    @Mapping(target = "id", source = "snippet.topLevelComment.id")
    @Mapping(target = "videoId", source = "snippet.topLevelComment.snippet.videoId")
    @Mapping(target = "author", source = "snippet.topLevelComment")
    @Mapping(target = "textDisplay", source = "snippet.topLevelComment.snippet.textDisplay")
    @Mapping(target = "textOriginal", source = "snippet.topLevelComment.snippet.textOriginal")
    @Mapping(target = "replyCount", source = "snippet.totalReplyCount")
    Comment fromYouTube(CommentThread commentThread);

    @Mapping(target = "id", source = "snippet.authorChannelId.value")
    @Mapping(target = "displayName", source = "snippet.authorDisplayName")
    @Mapping(target = "profileImageUrl", source = "snippet.authorProfileImageUrl")
    @Mapping(target = "channelUrl", source = "snippet.authorChannelUrl")
    Author getAuthor(com.google.api.services.youtube.model.Comment comment);
}

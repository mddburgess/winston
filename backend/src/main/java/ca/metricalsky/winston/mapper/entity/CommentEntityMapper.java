package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.entity.CommentEntity;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentThread;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
        AuthorEntityMapper.class,
        OffsetDateTimeMapper.class
})
public abstract class CommentEntityMapper {

    public CommentEntity toCommentEntity(CommentThread commentThread) {
        if (commentThread == null || commentThread.getSnippet() == null) {
            return null;
        }

        var comment = toCommentEntity(commentThread.getSnippet().getTopLevelComment());
        if (comment == null) {
            return null;
        }

        comment.setTotalReplyCount(commentThread.getSnippet().getTotalReplyCount());

        if (commentThread.getReplies() != null && commentThread.getReplies().getComments() != null) {
            var replies = commentThread.getReplies().getComments()
                    .stream()
                    .map(this::toCommentEntity)
                    .toList();
            comment.setReplies(replies);
        }

        return comment;
    }

    @Mapping(target = "videoId", source = "snippet.videoId")
    @Mapping(target = "parentId", source = "snippet.parentId")
    @Mapping(target = "author", source = "snippet")
    @Mapping(target = "textDisplay", source = "snippet.textDisplay")
    @Mapping(target = "textOriginal", source = "snippet.textOriginal")
    @Mapping(target = "publishedAt", source = "snippet.publishedAt")
    @Mapping(target = "updatedAt", source = "snippet.updatedAt")
    @Mapping(target = "lastFetchedAt", ignore = true)
    @Mapping(target = "totalReplyCount", ignore = true)
    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "replies", ignore = true)
    public abstract CommentEntity toCommentEntity(Comment ytComment);
}

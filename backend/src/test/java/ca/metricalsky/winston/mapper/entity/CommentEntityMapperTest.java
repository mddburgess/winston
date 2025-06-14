package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.entity.CommentEntity;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentSnippetAuthorChannelId;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadReplies;
import com.google.api.services.youtube.model.CommentThreadSnippet;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentEntityMapperTest {

    private static final String PUBLISHED_AT = "2025-01-01T00:00:00.000Z";
    private static final String UPDATED_AT = "2025-01-02T00:00:00.000Z";

    private final CommentEntityMapper commentEntityMapper = new CommentEntityMapperImpl();

    @Test
    void toCommentEntity_commentThread() {
        var commentThread = buildCommentThread();

        var comment = commentEntityMapper.toCommentEntity(commentThread);

        var topLevelComment = commentThread.getSnippet().getTopLevelComment();
        assertCommentProperties(comment, topLevelComment);
        assertThat(comment)
                .hasFieldOrPropertyWithValue("totalReplyCount", commentThread.getSnippet().getTotalReplyCount());
        assertThat(comment.getReplies()).hasSize(1);

        var replyComment = comment.getReplies().getFirst();
        var ytReplyComment = commentThread.getReplies().getComments().getFirst();
        assertCommentProperties(replyComment, ytReplyComment);
        assertThat(replyComment)
                .hasFieldOrPropertyWithValue("parentId", ytReplyComment.getSnippet().getParentId());
    }

    @Test
    void toCommentEntity_nullCommentThread() {
        var comment = commentEntityMapper.toCommentEntity((CommentThread) null);

        assertThat(comment)
                .isNull();
    }

    @Test
    void toCommentEntity_nullComment() {
        var comment = commentEntityMapper.toCommentEntity((Comment) null);

        assertThat(comment)
                .isNull();
    }

    @Test
    void toCommentEntity_emptyComment() {
        var comment = commentEntityMapper.toCommentEntity(new Comment());

        assertThat(comment)
                .hasAllNullFieldsOrProperties();
    }

    private static void assertCommentProperties(CommentEntity actual, Comment expected) {
        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", expected.getId())
                .hasFieldOrPropertyWithValue("videoId", expected.getSnippet().getVideoId())
                .hasFieldOrPropertyWithValue("textDisplay", expected.getSnippet().getTextDisplay())
                .hasFieldOrPropertyWithValue("textOriginal", expected.getSnippet().getTextOriginal())
                .hasFieldOrPropertyWithValue("publishedAt", OffsetDateTime.parse(PUBLISHED_AT))
                .hasFieldOrPropertyWithValue("updatedAt", OffsetDateTime.parse(UPDATED_AT));
        assertThat(actual.getAuthor())
                .hasFieldOrPropertyWithValue("id", expected.getSnippet().getAuthorChannelId().getValue())
                .hasFieldOrPropertyWithValue("displayName", expected.getSnippet().getAuthorDisplayName())
                .hasFieldOrPropertyWithValue("profileImageUrl", expected.getSnippet().getAuthorProfileImageUrl())
                .hasFieldOrPropertyWithValue("channelUrl", expected.getSnippet().getAuthorChannelUrl());
    }

    private static CommentThread buildCommentThread() {
        var snippet = new CommentThreadSnippet()
                .setTopLevelComment(buildTopLevelComment())
                .setTotalReplyCount(1L);

        var replies = new CommentThreadReplies()
                .setComments(List.of(buildReplyComment()));

        return new CommentThread()
                .setId("id")
                .setSnippet(snippet)
                .setReplies(replies);
    }

    private static Comment buildTopLevelComment() {
        var authorChannelId = new CommentSnippetAuthorChannelId()
                .setValue("value");

        var snippet = new CommentSnippet()
                .setAuthorChannelId(authorChannelId)
                .setAuthorDisplayName("authorDisplayName")
                .setAuthorProfileImageUrl("authorProfileImageUrl")
                .setAuthorChannelUrl("authorChannelUrl")
                .setTextDisplay("textDisplay")
                .setTextOriginal("textOriginal")
                .setPublishedAt(new DateTime(Instant.parse(PUBLISHED_AT).toEpochMilli(), 0))
                .setUpdatedAt(new DateTime(Instant.parse(UPDATED_AT).toEpochMilli(), 0))
                .setVideoId("videoId");

        return new Comment()
                .setId("id")
                .setSnippet(snippet);
    }

    private static Comment buildReplyComment() {
        var authorChannelId = new CommentSnippetAuthorChannelId()
                .setValue("value");

        var snippet = new CommentSnippet()
                .setAuthorChannelId(authorChannelId)
                .setAuthorDisplayName("authorDisplayName")
                .setAuthorProfileImageUrl("authorProfileImageUrl")
                .setAuthorChannelUrl("authorChannelUrl")
                .setTextDisplay("textDisplay")
                .setTextOriginal("textOriginal")
                .setPublishedAt(new DateTime(Instant.parse(PUBLISHED_AT).toEpochMilli(), 0))
                .setUpdatedAt(new DateTime(Instant.parse(UPDATED_AT).toEpochMilli(), 0))
                .setVideoId("videoId")
                .setParentId("parentId");

        return new Comment()
                .setId("id")
                .setSnippet(snippet);
    }
}

package ca.metricalsky.yt.comments.mapper.entity;

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

public class CommentMapperTest {

    private static final String PUBLISHED_AT = "2025-01-01T00:00:00.000Z";
    private static final String UPDATED_AT = "2025-01-02T00:00:00.000Z";

    private final CommentMapper commentMapper = new CommentMapperImpl();

    @Test
    void fromYouTubeCommentThread() {
        var ytCommentThread = buildYouTubeCommentThread();

        var comment = commentMapper.fromYouTube(ytCommentThread);

        var ytTopLevelComment = ytCommentThread.getSnippet().getTopLevelComment();
        assertCommentProperties(comment, ytTopLevelComment);
        assertThat(comment)
                .hasFieldOrPropertyWithValue("totalReplyCount", ytCommentThread.getSnippet().getTotalReplyCount());
        assertThat(comment.getReplies()).hasSize(1);

        var replyComment = comment.getReplies().getFirst();
        var ytReplyComment = ytCommentThread.getReplies().getComments().getFirst();
        assertCommentProperties(replyComment, ytReplyComment);
        assertThat(replyComment)
                .hasFieldOrPropertyWithValue("parentId", ytReplyComment.getSnippet().getParentId());
    }

    @Test
    void fromYouTubeCommentThread_nullCommentThread() {
        var comment = commentMapper.fromYouTube((CommentThread) null);
        assertThat(comment).isNull();
    }

    @Test
    void fromYouTubeComment_nullComment() {
        var comment = commentMapper.fromYouTube((Comment) null);
        assertThat(comment).isNull();
    }

    @Test
    void fromYouTubeComment_emptyComment() {
        var comment = commentMapper.fromYouTube(new Comment());
        assertThat(comment).isNotNull()
                .hasAllNullFieldsOrProperties();
    }

    private static void assertCommentProperties(ca.metricalsky.yt.comments.entity.Comment actual, Comment expected) {
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

    private static CommentThread buildYouTubeCommentThread() {
        var snippet = new CommentThreadSnippet()
                .setTopLevelComment(buildYouTubeTopLevelComment())
                .setTotalReplyCount(1L);

        var replies = new CommentThreadReplies()
                .setComments(List.of(buildYouTubeReplyComment()));

        return new CommentThread()
                .setId("id")
                .setSnippet(snippet)
                .setReplies(replies);
    }

    private static Comment buildYouTubeTopLevelComment() {
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

    private static Comment buildYouTubeReplyComment() {
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

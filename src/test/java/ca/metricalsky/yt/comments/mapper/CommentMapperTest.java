package ca.metricalsky.yt.comments.mapper;

import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentSnippetAuthorChannelId;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadSnippet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentMapperTest {

    private final CommentMapper commentMapper = new CommentMapperImpl();

    @Test
    void fromYouTube() {
        var ytCommentThread = buildYouTubeCommentThread();
        var ytTopLevelComment = ytCommentThread.getSnippet().getTopLevelComment();

        var comment = commentMapper.fromYouTube(ytCommentThread);

        assertThat(comment)
                .hasFieldOrPropertyWithValue("id", ytTopLevelComment.getId())
                .hasFieldOrPropertyWithValue("videoId", ytTopLevelComment.getSnippet().getVideoId())
                .hasFieldOrPropertyWithValue("textDisplay", ytTopLevelComment.getSnippet().getTextDisplay())
                .hasFieldOrPropertyWithValue("textOriginal", ytTopLevelComment.getSnippet().getTextOriginal())
                .hasFieldOrPropertyWithValue("replyCount", ytCommentThread.getSnippet().getTotalReplyCount());


        assertThat(comment.getAuthor())
                .hasFieldOrPropertyWithValue("id",
                        ytTopLevelComment.getSnippet().getAuthorChannelId().getValue())
                .hasFieldOrPropertyWithValue("displayName",
                        ytTopLevelComment.getSnippet().getAuthorDisplayName())
                .hasFieldOrPropertyWithValue("profileImageUrl",
                        ytTopLevelComment.getSnippet().getAuthorProfileImageUrl())
                .hasFieldOrPropertyWithValue("channelUrl",
                        ytTopLevelComment.getSnippet().getAuthorChannelUrl());
    }

    private static CommentThread buildYouTubeCommentThread() {
        var snippet = new CommentThreadSnippet()
                .setTopLevelComment(buildYouTubeTopLevelComment())
                .setTotalReplyCount(1L);

        return new CommentThread()
                .setId("commentThread.id")
                .setSnippet(snippet);
    }

    private static Comment buildYouTubeTopLevelComment() {
        var authorChannelId = new CommentSnippetAuthorChannelId()
                .setValue("commentThread.topLevelComment.snippet.authorChannelId.value");

        var snippet = new CommentSnippet()
                .setAuthorChannelId(authorChannelId)
                .setAuthorDisplayName("commentThread.topLevelComment.snippet.authorDisplayName")
                .setAuthorProfileImageUrl("commentThread.topLevelComment.snippet.authorProfileImageUrl")
                .setAuthorChannelUrl("commentThread.topLevelComment.snippet.authorChannelUrl")
                .setTextDisplay("commentThread.topLevelComment.snippet.textDisplay")
                .setTextOriginal("commentThread.topLevelComment.snippet.textOriginal")
                .setVideoId("commentThread.topLevelComment.snippet.videoId");

        return new Comment()
                .setId("commentThread.topLevelComment.id")
                .setSnippet(snippet);
    }
}

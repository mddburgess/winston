package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.database.entity.author.AuthorEntity;
import ca.metricalsky.winston.database.entity.comment.CommentPropertiesEntity;
import ca.metricalsky.winston.database.entity.comment.CommentEntity;
import ca.metricalsky.winston.test.TestUtils;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    private static final WinstonFaker faker = new WinstonFaker();

    @InjectMocks
    private final CommentMapper commentMapper = new CommentMapperImpl();

    @Spy
    private final AuthorMapper authorMapper = new AuthorMapperImpl();

    @Test
    void toTopLevelComment() {
        var commentEntity = buildCommentEntity();
        var authorEntity = commentEntity.getAuthor();

        var topLevelComment = commentMapper.toTopLevelComment(commentEntity);

        assertThat(topLevelComment)
                .hasFieldOrPropertyWithValue("id", commentEntity.getId())
                .hasFieldOrPropertyWithValue("videoId", commentEntity.getVideoId())
                .hasFieldOrPropertyWithValue("author.id", authorEntity.getId())
                .hasFieldOrPropertyWithValue("author.handle", authorEntity.getDisplayName())
                .hasFieldOrPropertyWithValue("author.channelUrl", authorEntity.getChannelUrl())
                .hasFieldOrPropertyWithValue("author.profileImageUrl",
                        "/api/v1/authors/" + authorEntity.getId() + "/thumbnail")
                .hasFieldOrPropertyWithValue("text.display", commentEntity.getTextDisplay())
                .hasFieldOrPropertyWithValue("text.original", commentEntity.getTextOriginal())
                .hasFieldOrPropertyWithValue("likeCount", commentEntity.getLikeCount())
                .hasFieldOrPropertyWithValue("publishedAt", commentEntity.getPublishedAt())
                .hasFieldOrPropertyWithValue("updatedAt", commentEntity.getUpdatedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", commentEntity.getLastFetchedAt())
                .hasFieldOrPropertyWithValue("properties.important", commentEntity.getProperties().getImportant())
                .hasFieldOrPropertyWithValue("properties.hidden", commentEntity.getProperties().getHidden())
                .hasFieldOrPropertyWithValue("totalReplyCount", commentEntity.getTotalReplyCount().intValue())
                .hasNoNullFieldsOrPropertiesExcept("replies");
    }

    @Test
    void toTopLevelComment_withReply() {
        var replyEntity = buildCommentEntity();
        var replyAuthor = replyEntity.getAuthor();

        var commentEntity = buildCommentEntity();
        commentEntity.setReplies(List.of(replyEntity));

        var topLevelComment = commentMapper.toTopLevelComment(commentEntity);

        assertThat(topLevelComment.getReplies()).first()
                .hasFieldOrPropertyWithValue("id", replyEntity.getId())
                .hasFieldOrPropertyWithValue("videoId", replyEntity.getVideoId())
                .hasFieldOrPropertyWithValue("author.id", replyAuthor.getId())
                .hasFieldOrPropertyWithValue("author.handle", replyAuthor.getDisplayName())
                .hasFieldOrPropertyWithValue("author.channelUrl", replyAuthor.getChannelUrl())
                .hasFieldOrPropertyWithValue("author.profileImageUrl",
                        "/api/v1/authors/" + replyAuthor.getId() + "/thumbnail")
                .hasFieldOrPropertyWithValue("text.display", replyEntity.getTextDisplay())
                .hasFieldOrPropertyWithValue("text.original", replyEntity.getTextOriginal())
                .hasFieldOrPropertyWithValue("likeCount", replyEntity.getLikeCount())
                .hasFieldOrPropertyWithValue("publishedAt", replyEntity.getPublishedAt())
                .hasFieldOrPropertyWithValue("updatedAt", replyEntity.getUpdatedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", replyEntity.getLastFetchedAt())
                .hasFieldOrPropertyWithValue("properties.important", replyEntity.getProperties().getImportant())
                .hasFieldOrPropertyWithValue("properties.hidden", replyEntity.getProperties().getHidden())
                .hasNoNullFieldsOrProperties();
    }

    @Test
    void toTopLevelComment_nullComment() {
        var topLevelComment = commentMapper.toTopLevelComment(null);

        assertThat(topLevelComment)
                .isNull();
    }

    @Test
    void toTopLevelComment_emptyComment() {
        var topLevelComment = commentMapper.toTopLevelComment(new CommentEntity());

        assertThat(topLevelComment)
                .hasAllNullFieldsOrPropertiesExcept("text", "properties");
        assertThat(topLevelComment.getText())
                .hasAllNullFieldsOrProperties();
        assertThat(topLevelComment.getProperties())
                .hasFieldOrPropertyWithValue("important", false)
                .hasFieldOrPropertyWithValue("hidden", false);
    }

    @Test
    void toComment_nullComment() {
        var comment = commentMapper.toComment(null);

        assertThat(comment)
                .isNull();
    }

    @Test
    void toComment_emptyComment() {
        var comment = commentMapper.toComment(new CommentEntity());

        assertThat(comment)
                .hasAllNullFieldsOrPropertiesExcept("text", "properties");
        assertThat(comment.getText())
                .hasAllNullFieldsOrProperties();
        assertThat(comment.getProperties())
                .hasFieldOrPropertyWithValue("important", false)
                .hasFieldOrPropertyWithValue("hidden", false);
    }

    private static CommentEntity buildCommentEntity() {
        var commentId = faker.youtube().commentId();
        var authorEntity = new AuthorEntity(
                TestUtils.randomId(),
                TestUtils.randomString(),
                TestUtils.randomString(),
                TestUtils.randomString(),
                Set.of()
        );
        var commentPropertiesEntity = new CommentPropertiesEntity(commentId, true, false);
        var commentEntity = new CommentEntity();
        commentEntity.setId(commentId);
        commentEntity.setVideoId(TestUtils.randomId());
        commentEntity.setAuthor(authorEntity);
        commentEntity.setTextDisplay(TestUtils.randomString());
        commentEntity.setTextOriginal(TestUtils.randomString());
        commentEntity.setLikeCount(TestUtils.randomLong());
        commentEntity.setPublishedAt(OffsetDateTime.now());
        commentEntity.setUpdatedAt(OffsetDateTime.now());
        commentEntity.setLastFetchedAt(OffsetDateTime.now());
        commentEntity.setProperties(commentPropertiesEntity);
        commentEntity.setTotalReplyCount(1L);
        return commentEntity;
    }
}

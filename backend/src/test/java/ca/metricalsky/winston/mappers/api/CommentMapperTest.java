package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.CommentEntity;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

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
                .hasFieldOrPropertyWithValue("text", commentEntity.getTextDisplay())
                .hasFieldOrPropertyWithValue("publishedAt", commentEntity.getPublishedAt())
                .hasFieldOrPropertyWithValue("updatedAt", commentEntity.getUpdatedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", commentEntity.getLastFetchedAt())
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
                .hasFieldOrPropertyWithValue("text", replyEntity.getTextDisplay())
                .hasFieldOrPropertyWithValue("publishedAt", replyEntity.getPublishedAt())
                .hasFieldOrPropertyWithValue("updatedAt", replyEntity.getUpdatedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", replyEntity.getLastFetchedAt())
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
                .hasAllNullFieldsOrProperties();
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
                .hasAllNullFieldsOrProperties();
    }

    private static CommentEntity buildCommentEntity() {
        var authorEntity = AuthorEntity.builder()
                .id(TestUtils.randomId())
                .displayName(TestUtils.randomString())
                .channelUrl(TestUtils.randomString())
                .profileImageUrl(TestUtils.randomString())
                .build();
        return CommentEntity.builder()
                .id(TestUtils.randomId())
                .videoId(TestUtils.randomId())
                .author(authorEntity)
                .textDisplay(TestUtils.randomString())
                .publishedAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .lastFetchedAt(OffsetDateTime.now())
                .totalReplyCount(1L)
                .build();
    }
}

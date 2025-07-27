package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.test.TestUtils;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentSnippetAuthorChannelId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorEntityMapperTest {

    private final AuthorEntityMapper authorEntityMapper = new AuthorEntityMapperImpl();

    @Test
    void toAuthorEntity() {
        var commentSnippet = buildCommentSnippet();

        var authorEntity = authorEntityMapper.toAuthorEntity(commentSnippet);

        assertThat(authorEntity)
                .hasFieldOrPropertyWithValue("id", commentSnippet.getAuthorChannelId().getValue())
                .hasFieldOrPropertyWithValue("displayName", commentSnippet.getAuthorDisplayName())
                .hasFieldOrPropertyWithValue("channelUrl", commentSnippet.getAuthorChannelUrl())
                .hasFieldOrPropertyWithValue("profileImageUrl", commentSnippet.getAuthorProfileImageUrl())
                .hasNoNullFieldsOrPropertiesExcept("lastFetchedAt");
    }

    @Test
    void toAuthorEntity_nullCommentSnippet() {
        var authorEntity = authorEntityMapper.toAuthorEntity(null);

        assertThat(authorEntity)
                .isNull();
    }

    @Test
    void toAuthorEntity_emptyCommentSnippet() {
        var authorEntity = authorEntityMapper.toAuthorEntity(new CommentSnippet());

        assertThat(authorEntity)
                .hasAllNullFieldsOrProperties();
    }

    private static CommentSnippet buildCommentSnippet() {
        var authorChannelId = new CommentSnippetAuthorChannelId()
                .setValue(TestUtils.randomId());

        return new CommentSnippet()
                .setAuthorChannelId(authorChannelId)
                .setAuthorDisplayName(TestUtils.randomString())
                .setAuthorChannelUrl(TestUtils.randomString())
                .setAuthorProfileImageUrl(TestUtils.randomString());
    }
}

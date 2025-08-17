package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.view.AuthorDetailsView;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public class AuthorMapperTest {

    private final AuthorMapper authorMapper = new AuthorMapperImpl();

    @Test
    void toAuthor_authorEntity() {
        var authorEntity = buildAuthorEntity();

        var author = authorMapper.toAuthor(authorEntity);

        assertThat(author)
                .as("author")
                .hasFieldOrPropertyWithValue("id", authorEntity.getId())
                .hasFieldOrPropertyWithValue("handle", authorEntity.getDisplayName())
                .hasFieldOrPropertyWithValue("channelUrl", authorEntity.getChannelUrl())
                .hasFieldOrPropertyWithValue("profileImageUrl",
                        "/api/v1/authors/" + authorEntity.getId() + "/thumbnail");
        assertThat(author.getAuthorStatistics())
                .as("author.authorStatistics")
                .isNull();
    }

    @Test
    void toAuthor_nullAuthorEntity() {
        var author = authorMapper.toAuthor((AuthorEntity) null);

        assertThat(author)
                .isNull();
    }

    @Test
    void toAuthor_emptyAuthorEntity() {
        var author = authorMapper.toAuthor(new AuthorEntity());

        assertThat(author)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void toAuthor_authorDetailsView() {
        var authorDetailsView = mockAuthorDetailsView();
        var authorEntity = authorDetailsView.getAuthor();

        var author = authorMapper.toAuthor(authorDetailsView);

        assertThat(author)
                .as("author")
                .hasFieldOrPropertyWithValue("id", authorEntity.getId())
                .hasFieldOrPropertyWithValue("handle", authorEntity.getDisplayName())
                .hasFieldOrPropertyWithValue("channelUrl", authorEntity.getChannelUrl())
                .hasFieldOrPropertyWithValue("profileImageUrl",
                        "/api/v1/authors/" + authorEntity.getId() + "/thumbnail");
        assertThat(author.getAuthorStatistics())
                .as("author.authorStatistics")
                .hasFieldOrPropertyWithValue("videoCount", authorDetailsView.getVideoCount())
                .hasFieldOrPropertyWithValue("commentCount", authorDetailsView.getCommentCount())
                .hasFieldOrPropertyWithValue("replyCount", authorDetailsView.getReplyCount());
    }

    @Test
    void toAuthor_nullAuthorDetailsView() {
        var author = authorMapper.toAuthor((AuthorDetailsView) null);

        assertThat(author)
                .isNull();
    }

    @ParameterizedTest
    @CsvSource({
            "@displayName, https://www.example.com/@displayName, authorId, @displayName",
            " , https://www.example.com/c/channelUrl, authorId, channelUrl",
            " , https://www.example.com/invalidChannelUrl, authorId, authorId",
            " , , authorId, authorId",
            " , , , "
    })
    void toAuthor_mapHandle(String displayName, String channelUrl, String id, String expectedHandle) {
        var authorEntity = AuthorEntity.builder()
                .id(id)
                .displayName(displayName)
                .channelUrl(channelUrl)
                .build();

        var author = authorMapper.toAuthor(authorEntity);

        assertThat(author.getHandle())
                .isEqualTo(expectedHandle);
    }

    private static AuthorEntity buildAuthorEntity() {
        return AuthorEntity.builder()
                .id(TestUtils.randomId())
                .displayName(TestUtils.randomString())
                .channelUrl(TestUtils.randomString())
                .profileImageUrl(TestUtils.randomString())
                .build();
    }

    private static AuthorDetailsView mockAuthorDetailsView() {
        var authorDetailsView = mock(AuthorDetailsView.class);
        lenient().when(authorDetailsView.getAuthor())
                .thenReturn(buildAuthorEntity());
        lenient().when(authorDetailsView.getVideoCount())
                .thenReturn(1L);
        lenient().when(authorDetailsView.getCommentCount())
                .thenReturn(2L);
        lenient().when(authorDetailsView.getReplyCount())
                .thenReturn(3L);
        return authorDetailsView;
    }
}

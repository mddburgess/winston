package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.database.entity.author.AuthorEntity;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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

    @ParameterizedTest
    @CsvSource({
            "@displayName, https://www.example.com/@displayName, authorId, @displayName",
            " , https://www.example.com/c/channelUrl, authorId, channelUrl",
            " , https://www.example.com/invalidChannelUrl, authorId, authorId",
            " , , authorId, authorId",
            " , , , "
    })
    void toAuthor_mapHandle(String displayName, String channelUrl, String id, String expectedHandle) {
        var authorEntity = new AuthorEntity(id, displayName, channelUrl, null, null, null);

        var author = authorMapper.toAuthor(authorEntity);

        assertThat(author.getHandle())
                .isEqualTo(expectedHandle);
    }

    private static AuthorEntity buildAuthorEntity() {
        return new AuthorEntity(
                TestUtils.randomId(),
                TestUtils.randomString(),
                TestUtils.randomString(),
                TestUtils.randomString(),
                OffsetDateTime.now(),
                Set.of());
    }
}

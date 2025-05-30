package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.entity.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorDtoMapperTest {

    private final AuthorDtoMapper authorDtoMapper = new AuthorDtoMapperImpl();

    @Test
    void toAuthorDto() {
        var author = buildAuthor();

        var authorDto = authorDtoMapper.toAuthorDto(author);

        assertThat(authorDto)
                .hasFieldOrPropertyWithValue("id", author.getId())
                .hasFieldOrPropertyWithValue("displayName", author.getDisplayName())
                .hasFieldOrPropertyWithValue("channelUrl", author.getChannelUrl())
                .hasFieldOrPropertyWithValue("profileImageUrl", "/api/v1/authors/" + author.getId() + "/thumbnail");
        assertThat(authorDto.getStatistics())
                .isNull();
    }

    @Test
    void toAuthorDto_nullAuthor() {
        var authorDto = authorDtoMapper.toAuthorDto((AuthorEntity) null);
        assertThat(authorDto).isNull();
    }

    @Test
    void toAuthorDto_emptyAuthor() {
        var authorDto = authorDtoMapper.toAuthorDto(new AuthorEntity());
        assertThat(authorDto)
                .hasNoNullFieldsOrPropertiesExcept("statistics");
    }

    @ParameterizedTest
    @CsvSource({
            "@displayName, https://www.example.com/@displayName, authorId, @displayName",
            ", https://www.example.com/c/channelUrl, authorId, channelUrl",
            ", https://www.example.com/invalidChannelUrl, authorId, authorId",
            ", , authorId, authorId",
    })
    void mapDisplayName(String displayName, String channelUrl, String id, String expectedDisplayName) {
        var author = new AuthorEntity();
        author.setId(id);
        author.setDisplayName(displayName);
        author.setChannelUrl(channelUrl);

        var mappedDisplayName = authorDtoMapper.mapDisplayName(author);

        assertThat(mappedDisplayName).isEqualTo(expectedDisplayName);
    }

    private static AuthorEntity buildAuthor() {
        var author = new AuthorEntity();
        author.setId("id");
        author.setDisplayName("displayName");
        author.setChannelUrl("channelUrl");
        author.setProfileImageUrl("profileImageUrl");
        return author;
    }
}

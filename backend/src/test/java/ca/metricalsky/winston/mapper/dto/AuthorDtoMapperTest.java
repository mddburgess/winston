package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.entity.Author;
import org.junit.jupiter.api.Test;

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
                .hasFieldOrPropertyWithValue("profileImageUrl", author.getProfileImageUrl());
    }

    @Test
    void toAuthorDto_nullAuthor() {
        var authorDto = authorDtoMapper.toAuthorDto(null);
        assertThat(authorDto).isNull();
    }

    @Test
    void toAuthorDto_emptyAuthor() {
        var authorDto = authorDtoMapper.toAuthorDto(new Author());
        assertThat(authorDto).hasNoNullFieldsOrProperties();
    }

    private static Author buildAuthor() {
        var author = new Author();
        author.setId("id");
        author.setDisplayName("displayName");
        author.setChannelUrl("channelUrl");
        author.setProfileImageUrl("profileImageUrl");
        return author;
    }
}

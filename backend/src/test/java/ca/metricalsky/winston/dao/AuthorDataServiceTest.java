package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.database.entity.author.AuthorEntity;
import ca.metricalsky.winston.database.repository.author.AuthorRepository;
import ca.metricalsky.winston.mappers.api.AuthorMapper;
import ca.metricalsky.winston.mappers.api.AuthorMapperImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorDataServiceTest {

    private static final String AUTHOR_ID = "authorId";
    private static final String AUTHOR_DISPLAY_NAME = "authorDisplayName";
    private static final String AUTHOR_CHANNEL_URL = "http://www.youtube.com/c/" + AUTHOR_DISPLAY_NAME;

    @InjectMocks
    private AuthorDataService authorDataService;

    @Spy
    private AuthorMapper authorMapper = new AuthorMapperImpl();
    @Mock
    private AuthorRepository authorRepository;

    @Test
    @Disabled
    void findAuthorByHandle_foundByDisplayName() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(buildAuthorEntity(AUTHOR_DISPLAY_NAME));

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).get()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID);

        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    @Disabled
    void findAuthorByHandle_foundByChannelUrl() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(null);
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(buildAuthorEntity(AUTHOR_DISPLAY_NAME));

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).get()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID);

        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    @Disabled
    void findAuthorByHandle_foundById() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(null);
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(null);
        when(authorRepository.getReferenceById(AUTHOR_DISPLAY_NAME))
                .thenReturn(buildAuthorEntity(AUTHOR_DISPLAY_NAME));

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).get()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID);
    }

    @Test
    @Disabled
    void findAuthorByHandle_notFound() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(null);
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(null);
        when(authorRepository.findById(AUTHOR_DISPLAY_NAME))
                .thenReturn(null);

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).isEmpty();
    }

    private static AuthorEntity buildAuthorEntity(String displayName) {
        return new AuthorEntity(
                AUTHOR_ID,
                displayName,
                AUTHOR_CHANNEL_URL,
                "profileImageUrl",
                Set.of()
        );
    }
}

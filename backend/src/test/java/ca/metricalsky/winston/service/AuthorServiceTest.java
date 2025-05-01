package ca.metricalsky.winston.service;

import ca.metricalsky.winston.entity.Author;
import ca.metricalsky.winston.entity.view.AuthorDetails;
import ca.metricalsky.winston.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    private static final String AUTHOR_ID = "authorId";
    private static final String AUTHOR_DISPLAY_NAME = "authorDisplayName";
    private static final String AUTHOR_CHANNEL_URL = "http://www.youtube.com/c/" + AUTHOR_DISPLAY_NAME;

    @InjectMocks
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorDetails authorDetails;

    @Test
    void findAll() {
        when(authorDetails.getAuthor())
                .thenReturn(buildAuthor());
        when(authorRepository.findAllAuthorDetails())
                .thenReturn(List.of(authorDetails));

        var authorDtos = authorService.findAll();

        assertThat(authorDtos)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID);
    }

    @Test
    void findAll_empty() {
        when(authorRepository.findAllAuthorDetails())
                .thenReturn(List.of());

        var authorDtos = authorService.findAll();

        assertThat(authorDtos).isEmpty();
    }

    @Test
    void findByHandle_foundByDisplayName() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.of(buildAuthor()));

        var authorDto = authorService.findByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).isPresent()
                .get().hasFieldOrPropertyWithValue("id", AUTHOR_ID);

        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void findByHandle_foundByChannelUrl() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(Optional.of(buildAuthor()));

        var authorDto = authorService.findByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).isPresent()
                .get().hasFieldOrPropertyWithValue("id", AUTHOR_ID);

        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void findByHandle_foundById() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(Optional.empty());
        when(authorRepository.findById(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.of(buildAuthor()));

        var authorDto = authorService.findByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).isPresent()
                .get().hasFieldOrPropertyWithValue("id", AUTHOR_ID);
    }

    @Test
    void findByHandle_notFound() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(Optional.empty());
        when(authorRepository.findById(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());

        var authorDto = authorService.findByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).isEmpty();
    }

    private static Author buildAuthor() {
        var author = new Author();
        author.setId(AUTHOR_ID);
        author.setChannelUrl(AUTHOR_CHANNEL_URL);
        author.setDisplayName(AUTHOR_DISPLAY_NAME);
        author.setProfileImageUrl("profileImageUrl");
        return author;
    }
}

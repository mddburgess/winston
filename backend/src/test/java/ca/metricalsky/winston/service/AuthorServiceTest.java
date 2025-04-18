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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    private static final String AUTHOR_ID = "authorId";

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
    void findById() {
        var author = new Author();
        author.setId(AUTHOR_ID);
        when(authorRepository.findById(AUTHOR_ID))
                .thenReturn(Optional.of(author));

        var authorDto = authorService.findById(AUTHOR_ID);

        assertThat(authorDto).isPresent()
                .get().hasFieldOrPropertyWithValue("id", AUTHOR_ID);
    }

    @Test
    void findById_notFound() {
        when(authorRepository.findById(AUTHOR_ID))
                .thenReturn(Optional.empty());

        var authorDto = authorService.findById(AUTHOR_ID);

        assertThat(authorDto).isEmpty();
    }

    private static Author buildAuthor() {
        var author = new Author();
        author.setId(AUTHOR_ID);
        author.setChannelUrl("channelUrl");
        author.setDisplayName("displayName");
        author.setProfileImageUrl("profileImageUrl");
        return author;
    }
}

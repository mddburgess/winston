package ca.metricalsky.winston.service;

import ca.metricalsky.winston.entity.Author;
import ca.metricalsky.winston.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}

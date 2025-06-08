package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.view.AuthorDetailsView;
import ca.metricalsky.winston.mapper.dto.AuthorMapper;
import ca.metricalsky.winston.mapper.dto.AuthorMapperImpl;
import ca.metricalsky.winston.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
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
    void getAllAuthors() {
        var authorDetailsView = mockAuthorDetailsView();
        when(authorRepository.findAllAuthorDetails())
                .thenReturn(List.of(authorDetailsView));

        var authors = authorDataService.getAllAuthors();

        assertThat(authors)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID)
                .hasFieldOrPropertyWithValue("statistics.videoCount", 1)
                .hasFieldOrPropertyWithValue("statistics.commentCount", 2)
                .hasFieldOrPropertyWithValue("statistics.replyCount", 3);
    }

    @Test
    void findAll_empty() {
        when(authorRepository.findAllAuthorDetails())
                .thenReturn(List.of());

        var authors = authorDataService.getAllAuthors();

        assertThat(authors).isEmpty();
    }

    @Test
    void findByHandle_foundByDisplayName() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.of(buildAuthorEntity()));

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).get()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID);

        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void findByHandle_foundByChannelUrl() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(Optional.of(buildAuthorEntity()));

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).get()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID);

        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void findByHandle_foundById() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(Optional.empty());
        when(authorRepository.findById(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.of(buildAuthorEntity()));

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).get()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID);
    }

    @Test
    void findByHandle_notFound() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(Optional.empty());
        when(authorRepository.findById(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).isEmpty();
    }

    private static AuthorEntity buildAuthorEntity() {
        return AuthorEntity.builder()
                .id(AUTHOR_ID)
                .channelUrl(AUTHOR_CHANNEL_URL)
                .displayName(AUTHOR_DISPLAY_NAME)
                .profileImageUrl("profileImageUrl")
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

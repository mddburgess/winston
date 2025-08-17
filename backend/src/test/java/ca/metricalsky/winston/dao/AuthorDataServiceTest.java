package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.view.AuthorDetailsView;
import ca.metricalsky.winston.mappers.api.AuthorMapper;
import ca.metricalsky.winston.mappers.api.AuthorMapperImpl;
import ca.metricalsky.winston.repository.AuthorRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
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
        var authorDetailsView = mockAuthorDetailsView(AUTHOR_DISPLAY_NAME);
        when(authorRepository.findAllAuthorDetails())
                .thenReturn(List.of(authorDetailsView));

        var authors = authorDataService.getAllAuthors();

        assertThat(authors).first()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID)
                .hasFieldOrPropertyWithValue("authorStatistics.videoCount", authorDetailsView.getVideoCount())
                .hasFieldOrPropertyWithValue("authorStatistics.commentCount", authorDetailsView.getCommentCount())
                .hasFieldOrPropertyWithValue("authorStatistics.replyCount", authorDetailsView.getReplyCount());
    }

    @Test
    void getAllAuthors_sortedByHandle() {
        var bob = mockAuthorDetailsView("@bob");
        var alice = mockAuthorDetailsView("@alice");
        when(authorRepository.findAllAuthorDetails())
                .thenReturn(List.of(bob, alice));

        var authors = authorDataService.getAllAuthors();

        assertThat(authors)
                .hasSize(2)
                .as("list should be sorted by handle")
                .isSortedAccordingTo(Comparator.comparing(Author::getHandle));
    }

    @Test
    void getAllAuthors_empty() {
        when(authorRepository.findAllAuthorDetails())
                .thenReturn(List.of());

        var authors = authorDataService.getAllAuthors();

        assertThat(authors).isEmpty();
    }

    @Test
    @Disabled
    void findAuthorByHandle_foundByDisplayName() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.of(buildAuthorEntity(AUTHOR_DISPLAY_NAME)));

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).get()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID);

        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    @Disabled
    void findAuthorByHandle_foundByChannelUrl() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(Optional.of(buildAuthorEntity(AUTHOR_DISPLAY_NAME)));

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).get()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID);

        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    @Disabled
    void findAuthorByHandle_foundById() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(Optional.empty());
        when(authorRepository.findById(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.of(buildAuthorEntity(AUTHOR_DISPLAY_NAME)));

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).get()
                .hasFieldOrPropertyWithValue("id", AUTHOR_ID);
    }

    @Test
    void findAuthorByHandle_notFound() {
        when(authorRepository.findByDisplayName(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());
        when(authorRepository.findByChannelUrl(AUTHOR_CHANNEL_URL))
                .thenReturn(Optional.empty());
        when(authorRepository.findById(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());

        var authorDto = authorDataService.findAuthorByHandle(AUTHOR_DISPLAY_NAME);

        assertThat(authorDto).isEmpty();
    }

    private static AuthorEntity buildAuthorEntity(String displayName) {
        return AuthorEntity.builder()
                .id(AUTHOR_ID)
                .channelUrl(AUTHOR_CHANNEL_URL)
                .displayName(displayName)
                .profileImageUrl("profileImageUrl")
                .build();
    }

    private static AuthorDetailsView mockAuthorDetailsView(String displayName) {
        var authorDetailsView = mock(AuthorDetailsView.class);
        lenient().when(authorDetailsView.getAuthor())
                .thenReturn(buildAuthorEntity(displayName));
        lenient().when(authorDetailsView.getVideoCount())
                .thenReturn(1L);
        lenient().when(authorDetailsView.getCommentCount())
                .thenReturn(2L);
        lenient().when(authorDetailsView.getReplyCount())
                .thenReturn(3L);
        return authorDetailsView;
    }
}

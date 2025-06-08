package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.entity.CommentEntity;
import ca.metricalsky.winston.entity.VideoEntity;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = AuthorRepository.class
))
class AuthorRepositoryTest {

    private static final String DISPLAY_NAME = "@displayName";
    private static final String CHANNEL_URL = "http://www.youtube.com/c/channelUrl";

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AuthorRepository repository;

    private AuthorEntity savedAuthor;

    @BeforeEach
    void beforeEach() {
        savedAuthor = entityManager.persist(AuthorEntity.builder()
                .id(TestUtils.randomId())
                .displayName(DISPLAY_NAME)
                .channelUrl(CHANNEL_URL)
                .build());
    }

    @Test
    void findByChannelUrl() {
        var author = repository.findByChannelUrl(CHANNEL_URL);

        assertThat(author)
                .contains(savedAuthor);
    }

    @Test
    void findByChannelUrl_notFound() {
        var author = repository.findByChannelUrl("");

        assertThat(author)
                .isEmpty();
    }

    @Test
    void findByDisplayName() {
        var author = repository.findByDisplayName(DISPLAY_NAME);

        assertThat(author)
                .contains(savedAuthor);
    }

    @Test
    void findByDisplayName_notFound() {
        var author = repository.findByDisplayName("");

        assertThat(author)
                .isEmpty();
    }

    @Test
    void findAllAuthorDetails() {
        var channel = entityManager.persist(ChannelEntity.builder()
                .id(TestUtils.randomId())
                .build());
        var video = entityManager.persist(VideoEntity.builder()
                .id(TestUtils.randomId())
                .channelId(channel.getId())
                .build());
        var comment = entityManager.persist(CommentEntity.builder()
                .id(TestUtils.randomId())
                .videoId(video.getId())
                .author(savedAuthor)
                .build());
        entityManager.persist(CommentEntity.builder()
                .id(TestUtils.randomId())
                .videoId(video.getId())
                .parentId(comment.getId())
                .author(savedAuthor)
                .build());

        var authorDetailsList = repository.findAllAuthorDetails();

        assertThat(authorDetailsList)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("author", savedAuthor)
                .hasFieldOrPropertyWithValue("videoCount", 1L)
                .hasFieldOrPropertyWithValue("commentCount", 1L)
                .hasFieldOrPropertyWithValue("videoCount", 1L);
    }

    @Test
    void findAllAuthorDetails_noComments() {
        var authorDetailsList = repository.findAllAuthorDetails();

        assertThat(authorDetailsList)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("author", savedAuthor)
                .hasFieldOrPropertyWithValue("videoCount", 0L)
                .hasFieldOrPropertyWithValue("commentCount", 0L)
                .hasFieldOrPropertyWithValue("videoCount", 0L);
    }
}

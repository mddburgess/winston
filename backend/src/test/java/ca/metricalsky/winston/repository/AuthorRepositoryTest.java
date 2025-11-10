package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.test.TestUtils;
import ca.metricalsky.winston.test.annotations.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
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
}

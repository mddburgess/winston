package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.AuthorEntity;
import org.apache.commons.lang3.RandomStringUtils;
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
        var author = new AuthorEntity();
        author.setId(RandomStringUtils.secure().nextAlphanumeric(10));
        author.setDisplayName(DISPLAY_NAME);
        author.setChannelUrl(CHANNEL_URL);
        savedAuthor = entityManager.persist(author);
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

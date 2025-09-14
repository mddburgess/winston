package ca.metricalsky.winston.repository;


import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.test.IntegrationTest;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class AuthorJdbcRepositoryTest {

    @Autowired
    private AuthorJdbcRepository authorJdbcRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @AfterEach
    void afterEach() {
        authorRepository.deleteAll();
    }

    @Test
    void saveAll() {
        var authorsToInsert = new ArrayList<AuthorEntity>();
        for (int i = 0; i < 5; ++i) {
            authorsToInsert.add(createAuthorEntity());
        }

        authorJdbcRepository.saveAll(authorsToInsert);

        assertThat(authorRepository.findAll()).hasSize(5);

        var authorsToInsertOrUpdate = new ArrayList<AuthorEntity>();
        for (var author: authorsToInsert) {
            authorsToInsertOrUpdate.add(copyAuthorEntity(author));
        }
        for (int i = 0; i < 5; ++i) {
            authorsToInsertOrUpdate.add(createAuthorEntity());
        }

        authorJdbcRepository.saveAll(authorsToInsertOrUpdate);

        assertThat(authorRepository.findAll()).hasSize(10);
    }

    private static AuthorEntity createAuthorEntity() {
        return AuthorEntity.builder()
                .id(TestUtils.randomId())
                .displayName(TestUtils.randomString())
                .channelUrl(TestUtils.randomString())
                .profileImageUrl(TestUtils.randomString())
                .build();
    }

    private static AuthorEntity copyAuthorEntity(AuthorEntity authorEntity) {
        return AuthorEntity.builder()
                .id(authorEntity.getId())
                .displayName(authorEntity.getDisplayName())
                .channelUrl(authorEntity.getChannelUrl())
                .profileImageUrl(authorEntity.getProfileImageUrl())
                .build();
    }
}

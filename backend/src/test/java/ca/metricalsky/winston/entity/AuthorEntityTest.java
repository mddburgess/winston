package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void persistsWithOnlyRequiredFields() {
        var authorEntity = AuthorEntity.builder()
                .id(TestUtils.randomId())
                .build();

        var persistedEntity = entityManager.persistFlushFind(authorEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", authorEntity.getId())
                .hasAllNullFieldsOrPropertiesExcept("id", "lastFetchedAt");
    }

    @Test
    void persistsWithAllOptionalFields() {
        var authorEntity = AuthorEntity.builder()
                .id(TestUtils.randomId())
                .displayName(TestUtils.randomString())
                .channelUrl(TestUtils.randomString())
                .profileImageUrl(TestUtils.randomString())
                .build();

        var persistedEntity = entityManager.persistFlushFind(authorEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", authorEntity.getId())
                .hasFieldOrPropertyWithValue("displayName", authorEntity.getDisplayName())
                .hasFieldOrPropertyWithValue("channelUrl", authorEntity.getChannelUrl())
                .hasFieldOrPropertyWithValue("profileImageUrl", authorEntity.getProfileImageUrl());
        assertThat(persistedEntity.getLastFetchedAt())
                .isNotNull();
    }
}

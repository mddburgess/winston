package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Set;

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
                .hasFieldOrPropertyWithValue("aliases", Set.of())
                .hasAllNullFieldsOrPropertiesExcept("aliases", "id", "lastFetchedAt");
    }

    @Test
    void persistsWithAllOptionalFields() {
        var authorAlias = TestUtils.randomString();
        var authorEntity = AuthorEntity.builder()
                .id(TestUtils.randomId())
                .displayName(TestUtils.randomString())
                .channelUrl(TestUtils.randomString())
                .profileImageUrl(TestUtils.randomString())
                .aliases(Set.of(authorAlias))
                .build();

        var persistedEntity = entityManager.persistFlushFind(authorEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", authorEntity.getId())
                .hasFieldOrPropertyWithValue("displayName", authorEntity.getDisplayName())
                .hasFieldOrPropertyWithValue("channelUrl", authorEntity.getChannelUrl())
                .hasFieldOrPropertyWithValue("profileImageUrl", authorEntity.getProfileImageUrl());
        assertThat(persistedEntity.getAliases())
                .containsExactly(authorAlias);
        assertThat(persistedEntity.getLastFetchedAt())
                .isNotNull();
    }
}

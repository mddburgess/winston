package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChannelEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void persistsWithOnlyRequiredFields() {
        var channelEntity = ChannelEntity.builder()
                .id(TestUtils.randomId())
                .build();

        var persistedEntity = entityManager.persistAndFlush(channelEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", channelEntity.getId())
                .hasAllNullFieldsOrPropertiesExcept("id", "lastFetchedAt");
    }

    @Test
    void persistsWithAllOptionalFields() {
        var channelEntity = ChannelEntity.builder()
                .id(TestUtils.randomId())
                .title(TestUtils.randomString())
                .description(TestUtils.randomString())
                .customUrl(TestUtils.randomString())
                .thumbnailUrl(TestUtils.randomString())
                .publishedAt(OffsetDateTime.now())
                .topics(Set.of(TestUtils.randomString()))
                .keywords(Set.of(TestUtils.randomString()))
                .build();

        var persistedEntity = entityManager.persistAndFlush(channelEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", channelEntity.getId())
                .hasFieldOrPropertyWithValue("title", channelEntity.getTitle())
                .hasFieldOrPropertyWithValue("description", channelEntity.getDescription())
                .hasFieldOrPropertyWithValue("customUrl", channelEntity.getCustomUrl())
                .hasFieldOrPropertyWithValue("thumbnailUrl", channelEntity.getThumbnailUrl())
                .hasFieldOrPropertyWithValue("publishedAt", channelEntity.getPublishedAt())
                .hasFieldOrPropertyWithValue("topics", channelEntity.getTopics())
                .hasFieldOrPropertyWithValue("keywords", channelEntity.getKeywords())
                .hasNoNullFieldsOrProperties();
    }
}

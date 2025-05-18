package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VideoEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void persistsWithOnlyRequiredFields() {
        var channelEntity = persistChannel();
        var videoEntity = VideoEntity.builder()
                .id(TestUtils.randomId())
                .channelId(channelEntity.getId())
                .build();

        var persistedEntity = entityManager.persistFlushFind(videoEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", videoEntity.getId())
                .hasFieldOrPropertyWithValue("channelId", videoEntity.getChannelId())
                .hasAllNullFieldsOrPropertiesExcept("id", "channelId", "lastFetchedAt");
    }

    @Test
    void persistsWithAllOptionalFields() {
        var channelEntity = persistChannel();
        var videoEntity = VideoEntity.builder()
                .id(TestUtils.randomId())
                .channelId(channelEntity.getId())
                .title(TestUtils.randomString())
                .description(TestUtils.randomString())
                .thumbnailUrl(TestUtils.randomString())
                .publishedAt(OffsetDateTime.now())
                .build();

        var persistedEntity = entityManager.persistFlushFind(videoEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", videoEntity.getId())
                .hasFieldOrPropertyWithValue("channelId", videoEntity.getChannelId())
                .hasFieldOrPropertyWithValue("title", videoEntity.getTitle())
                .hasFieldOrPropertyWithValue("description", videoEntity.getDescription())
                .hasFieldOrPropertyWithValue("thumbnailUrl", videoEntity.getThumbnailUrl())
                .hasFieldOrPropertyWithValue("publishedAt",
                        videoEntity.getPublishedAt().withOffsetSameInstant(ZoneOffset.UTC))
                .hasNoNullFieldsOrPropertiesExcept("comments");
    }

    private ChannelEntity persistChannel() {
        var channelEntity = ChannelEntity.builder()
                .id(TestUtils.randomId())
                .build();
        return entityManager.persist(channelEntity);
    }
}

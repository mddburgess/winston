package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.database.entity.channel.ChannelEntity;
import ca.metricalsky.winston.database.entity.video.VideoEntity;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VideoEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void persistsWithOnlyRequiredFields() {
        var channelEntity = persistChannel();
        var videoEntity = new VideoEntity();
        videoEntity.setId(TestUtils.randomId());
        videoEntity.setChannelId(channelEntity.getId());

        var persistedEntity = entityManager.persistFlushFind(videoEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", videoEntity.getId())
                .hasFieldOrPropertyWithValue("channelId", videoEntity.getChannelId())
                .hasAllNullFieldsOrPropertiesExcept("id", "channelId", "lastFetchedAt");
    }

    @Test
    void persistsWithAllOptionalFields() {
        var channelEntity = persistChannel();
        var videoEntity = new VideoEntity();
        videoEntity.setId(TestUtils.randomId());
        videoEntity.setChannelId(channelEntity.getId());
        videoEntity.setTitle(TestUtils.randomString());
        videoEntity.setDescription(TestUtils.randomString());
        videoEntity.setThumbnailUrl(TestUtils.randomString());
        videoEntity.setPublishedAt(OffsetDateTime.now());

        var persistedEntity = entityManager.persistFlushFind(videoEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", videoEntity.getId())
                .hasFieldOrPropertyWithValue("channelId", videoEntity.getChannelId())
                .hasFieldOrPropertyWithValue("title", videoEntity.getTitle())
                .hasFieldOrPropertyWithValue("description", videoEntity.getDescription())
                .hasFieldOrPropertyWithValue("thumbnailUrl", videoEntity.getThumbnailUrl())
                .hasNoNullFieldsOrPropertiesExcept("comments", "details");
    }

    private ChannelEntity persistChannel() {
        var channelEntity = new ChannelEntity();
        channelEntity.setId(TestUtils.randomId());
        return entityManager.persist(channelEntity);
    }
}

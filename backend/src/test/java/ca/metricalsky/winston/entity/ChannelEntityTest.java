package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.database.entity.channel.ChannelEntity;
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
        var channelEntity = new ChannelEntity();
        channelEntity.setId(TestUtils.randomId());

        var persistedEntity = entityManager.persistFlushFind(channelEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", channelEntity.getId())
                .hasAllNullFieldsOrPropertiesExcept("id", "keywords", "lastFetchedAt", "topics");
        assertThat(persistedEntity.getTopics())
                .isEmpty();
        assertThat(persistedEntity.getKeywords())
                .isEmpty();
    }

    @Test
    void persistsWithAllOptionalFields() {
        var channelEntity = new ChannelEntity();
        channelEntity.setId(TestUtils.randomId());
        channelEntity.setTitle(TestUtils.randomString());
        channelEntity.setDescription(TestUtils.randomString());
        channelEntity.setCustomUrl(TestUtils.randomString());
        channelEntity.setThumbnailUrl(TestUtils.randomString());
        channelEntity.setUploadsPlaylistId(TestUtils.randomId());
        channelEntity.setVideoCount(TestUtils.randomLong());
        channelEntity.setViewCount(TestUtils.randomLong());
        channelEntity.setSubscriberCount(TestUtils.randomLong());
        channelEntity.setPublishedAt(OffsetDateTime.now());
        channelEntity.setTopics(Set.of(TestUtils.randomString()));
        channelEntity.setKeywords(Set.of(TestUtils.randomString()));

        var persistedEntity = entityManager.persistFlushFind(channelEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", channelEntity.getId())
                .hasFieldOrPropertyWithValue("title", channelEntity.getTitle())
                .hasFieldOrPropertyWithValue("description", channelEntity.getDescription())
                .hasFieldOrPropertyWithValue("customUrl", channelEntity.getCustomUrl())
                .hasFieldOrPropertyWithValue("thumbnailUrl", channelEntity.getThumbnailUrl())
                .hasFieldOrPropertyWithValue("uploadsPlaylistId", channelEntity.getUploadsPlaylistId())
                .hasFieldOrPropertyWithValue("videoCount", channelEntity.getVideoCount())
                .hasFieldOrPropertyWithValue("viewCount", channelEntity.getViewCount())
                .hasFieldOrPropertyWithValue("subscriberCount", channelEntity.getSubscriberCount())
                .hasFieldOrPropertyWithValue("topics", channelEntity.getTopics())
                .hasFieldOrPropertyWithValue("keywords", channelEntity.getKeywords())
                .hasNoNullFieldsOrPropertiesExcept("properties");
    }
}

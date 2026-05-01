package ca.metricalsky.winston.convert.api;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.database.entity.channel.ChannelEntity;
import ca.metricalsky.winston.test.annotations.ConverterTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import java.net.URI;

import static ca.metricalsky.winston.test.factory.entity.ChannelEntityFactory.createChannelEntity;
import static org.assertj.core.api.Assertions.assertThat;

@ConverterTest
class ChannelEntityToChannelConverterTest {

    @Autowired
    private ConversionService conversionService;

    @Test
    void convert() {
        var channelEntity = createChannelEntity();
        var channel = conversionService.convert(channelEntity, Channel.class);

        assertThat(channel)
                .as("channel")
                .hasFieldOrPropertyWithValue("id", channelEntity.getId())
                .hasFieldOrPropertyWithValue("title", channelEntity.getTitle())
                .hasFieldOrPropertyWithValue("description", channelEntity.getDescription())
                .hasFieldOrPropertyWithValue("handle", channelEntity.getCustomUrl())
                .hasFieldOrPropertyWithValue("thumbnailUrl",
                        "/api/v1/channels/" + channelEntity.getId() + "/thumbnail")
                .hasFieldOrPropertyWithValue("publishedAt", channelEntity.getPublishedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", channelEntity.getLastFetchedAt())
                .hasFieldOrPropertyWithValue("properties.archived", channelEntity.getProperties().getArchived())
                .hasFieldOrPropertyWithValue("statistics.videoCount", channelEntity.getVideoCount())
                .hasFieldOrPropertyWithValue("statistics.viewCount", channelEntity.getViewCount())
                .hasFieldOrPropertyWithValue("statistics.subscriberCount", channelEntity.getSubscriberCount());

        assertThat(channel.getTopics())
                .as("channel.topics")
                .map(URI::toString)
                .containsExactlyInAnyOrderElementsOf(channelEntity.getTopics());

        assertThat(channel.getKeywords())
                .as("channel.keywords")
                .containsExactlyInAnyOrderElementsOf(channelEntity.getKeywords());

        assertThat(channel.getVideoCount())
                .as("channel.videoCount")
                .isNull();
    }

    @Test
    void convert_nullChannelEntity() {
        var channel = conversionService.convert(null, Channel.class);

        assertThat(channel)
                .isNull();
    }

    @Test
    void convert_emptyChannelEntity() {
        var channel = conversionService.convert(new ChannelEntity(), Channel.class);

        assertThat(channel)
                .hasAllNullFieldsOrPropertiesExcept("topics", "keywords", "lastFetchedAt", "properties", "statistics");
    }
}

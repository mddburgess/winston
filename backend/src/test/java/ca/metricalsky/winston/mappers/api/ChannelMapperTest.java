package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ChannelMapperTest {

    private final ChannelMapper channelMapper = new ChannelMapperImpl();

    @Test
    void toChannel() {
        var channelEntity = buildChannelEntity();

        var channel = channelMapper.toChannel(channelEntity);

        assertThat(channel)
                .as("channel")
                .hasFieldOrPropertyWithValue("id", channelEntity.getId())
                .hasFieldOrPropertyWithValue("title", channelEntity.getTitle())
                .hasFieldOrPropertyWithValue("description", channelEntity.getDescription())
                .hasFieldOrPropertyWithValue("handle", channelEntity.getCustomUrl())
                .hasFieldOrPropertyWithValue("thumbnailUrl",
                        "/api/v1/channels/" + channelEntity.getId() + "/thumbnail")
                .hasFieldOrPropertyWithValue("publishedAt", channelEntity.getPublishedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", channelEntity.getLastFetchedAt());
        assertThat(channel.getTopics())
                .as("channel.topics")
                .containsExactly(URI.create("https://www.example.com"));
        assertThat(channel.getKeywords())
                .as("channel.keywords")
                .containsExactlyInAnyOrderElementsOf(channelEntity.getKeywords());
        assertThat(channel.getVideoCount())
                .as("channel.videoCount")
                .isNull();
    }

    @Test
    void toChannel_nullChannelEntity() {
        var channel = channelMapper.toChannel(null);

        assertThat(channel)
                .isNull();
    }

    @Test
    void toChannel_emptyChannelEntity() {
        var channel = channelMapper.toChannel(new ChannelEntity());

        assertThat(channel)
                .hasAllNullFieldsOrProperties();
    }

    private static ChannelEntity buildChannelEntity() {
        return ChannelEntity.builder()
                .id(TestUtils.randomId())
                .title(TestUtils.randomString())
                .description(TestUtils.randomString())
                .customUrl(TestUtils.randomString())
                .thumbnailUrl(TestUtils.randomString())
                .topics(Set.of("https://www.example.com"))
                .keywords(Set.of(TestUtils.randomString(), TestUtils.randomString()))
                .publishedAt(OffsetDateTime.now())
                .lastFetchedAt(OffsetDateTime.now())
                .build();
    }
}

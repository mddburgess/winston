package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.entity.ChannelEntity;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ChannelDtoMapperTest {

    private final ChannelDtoMapper channelDtoMapper = new ChannelDtoMapperImpl();

    @Test
    void fromEntity() {
        var channel = buildChannel();

        var channelDto = channelDtoMapper.fromEntity(channel);

        assertThat(channelDto)
                .hasFieldOrPropertyWithValue("id", channel.getId())
                .hasFieldOrPropertyWithValue("title", channel.getTitle())
                .hasFieldOrPropertyWithValue("description", channel.getDescription())
                .hasFieldOrPropertyWithValue("customUrl", channel.getCustomUrl())
                .hasFieldOrPropertyWithValue("thumbnailUrl", "/api/v1/channels/" + channel.getId() + "/thumbnail")
                .hasFieldOrPropertyWithValue("videoCount", 0L)
                .hasFieldOrPropertyWithValue("publishedAt", channel.getPublishedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", channel.getLastFetchedAt());
    }

    @Test
    void fromEntity_nullChannel() {
        var channelDto = channelDtoMapper.fromEntity(null);
        assertThat(channelDto).isNull();
    }

    @Test
    void fromEntity_emptyChannel() {
        var channelDto = channelDtoMapper.fromEntity(new ChannelEntity());
        assertThat(channelDto)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    private static ChannelEntity buildChannel() {
        var channel = new ChannelEntity();
        channel.setId("id");
        channel.setTitle("title");
        channel.setDescription("description");
        channel.setCustomUrl("customUrl");
        channel.setThumbnailUrl("thumbnailUrl");
        channel.setTopics(Set.of("topic1", "topic2"));
        channel.setKeywords(Set.of("keyword1", "keyword2"));
        channel.setPublishedAt(OffsetDateTime.now());
        channel.setLastFetchedAt(OffsetDateTime.now());
        return channel;
    }
}

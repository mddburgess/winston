package ca.metricalsky.yt.comments.mapper.dto;

import ca.metricalsky.yt.comments.entity.Channel;
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
                .hasFieldOrPropertyWithValue("thumbnailUrl", "/api/channels/" + channel.getId() + "/thumbnail")
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
        var channelDto = channelDtoMapper.fromEntity(new Channel());
        assertThat(channelDto)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    private static Channel buildChannel() {
        var channel = new Channel();
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

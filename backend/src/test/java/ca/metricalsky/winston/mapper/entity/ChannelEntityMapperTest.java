package ca.metricalsky.winston.mapper.entity;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelBrandingSettings;
import com.google.api.services.youtube.model.ChannelSettings;
import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.ChannelTopicDetails;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChannelEntityMapperTest {

    private static final String PUBLISHED_AT = "2025-01-01T00:00:00.000Z";

    private final ChannelEntityMapper channelEntityMapper = new ChannelEntityMapperImpl();

    @Test
    void toChannelEntity() {
        var ytChannel = buildChannel();

        var channel = channelEntityMapper.toChannelEntity(ytChannel);

        assertThat(channel)
                .hasFieldOrPropertyWithValue("id", ytChannel.getId())
                .hasFieldOrPropertyWithValue("title", ytChannel.getSnippet().getTitle())
                .hasFieldOrPropertyWithValue("description", ytChannel.getSnippet().getDescription())
                .hasFieldOrPropertyWithValue("customUrl", ytChannel.getSnippet().getCustomUrl())
                .hasFieldOrPropertyWithValue("publishedAt", OffsetDateTime.parse(PUBLISHED_AT))
                .hasFieldOrPropertyWithValue("thumbnailUrl",
                        ytChannel.getSnippet().getThumbnails().getHigh().getUrl());
        assertThat(channel.getTopics())
                .containsOnlyOnceElementsOf(ytChannel.getTopicDetails().getTopicCategories());
        assertThat(channel.getKeywords())
                .containsOnlyOnce("keyword", "long keyword");
    }

    @Test
    void toChannelEntity_nullChannel() {
        var channel = channelEntityMapper.toChannelEntity(null);

        assertThat(channel)
                .isNull();
    }

    @Test
    void toChannelEntity_emptyChannel() {
        var channel = channelEntityMapper.toChannelEntity(new Channel());

        assertThat(channel)
                .hasAllNullFieldsOrProperties();
    }

    private static Channel buildChannel() {
        var thumbnail = new Thumbnail()
                .setUrl("channel.snippet.thumbnails.high.url");

        var thumbnails = new ThumbnailDetails()
                .setHigh(thumbnail);

        var snippet = new ChannelSnippet()
                .setTitle("channel.snippet.title")
                .setDescription("channel.snippet.description")
                .setCustomUrl("channel.snippet.customUrl")
                .setPublishedAt(new DateTime(Instant.parse(PUBLISHED_AT).toEpochMilli(), 0))
                .setThumbnails(thumbnails);

        var topicDetails = new ChannelTopicDetails()
                .setTopicCategories(List.of("channel.topicDetails.topicCategories"));

        var channelSettings = new ChannelSettings()
                .setKeywords("keyword \"long keyword\"");

        var brandingSettings = new ChannelBrandingSettings()
                .setChannel(channelSettings);

        return new Channel()
                .setId("channel.id")
                .setSnippet(snippet)
                .setTopicDetails(topicDetails)
                .setBrandingSettings(brandingSettings);
    }
}

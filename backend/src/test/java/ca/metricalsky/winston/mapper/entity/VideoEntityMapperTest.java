package ca.metricalsky.winston.mapper.entity;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityContentDetails;
import com.google.api.services.youtube.model.ActivityContentDetailsUpload;
import com.google.api.services.youtube.model.ActivitySnippet;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class VideoEntityMapperTest {

    private static final String PUBLISHED_AT = "2025-01-01T00:00:00.000Z";

    private final VideoEntityMapper videoEntityMapper = new VideoEntityMapperImpl();

    @Test
    void toVideoEntity() {
        var activity = buildActivity();

        var video = videoEntityMapper.toVideoEntity(activity);

        assertThat(video)
                .hasFieldOrPropertyWithValue("id", activity.getContentDetails().getUpload().getVideoId())
                .hasFieldOrPropertyWithValue("publishedAt", OffsetDateTime.parse(PUBLISHED_AT))
                .hasFieldOrPropertyWithValue("channelId", activity.getSnippet().getChannelId())
                .hasFieldOrPropertyWithValue("title", activity.getSnippet().getTitle())
                .hasFieldOrPropertyWithValue("description", activity.getSnippet().getDescription())
                .hasFieldOrPropertyWithValue("thumbnailUrl",
                        activity.getSnippet().getThumbnails().getHigh().getUrl());
    }

    @Test
    void toVideoEntity_nullActivity() {
        var videoEntity = videoEntityMapper.toVideoEntity(null);

        assertThat(videoEntity)
                .isNull();
    }

    @Test
    void toVideoEntity_emptyActivity() {
        var videoEntity = videoEntityMapper.toVideoEntity(new Activity());

        assertThat(videoEntity)
                .hasAllNullFieldsOrProperties();
    }

    private static Activity buildActivity() {
        var thumbnail = new Thumbnail()
                .setUrl("channel.snippet.thumbnails.high.url");

        var thumbnails = new ThumbnailDetails()
                .setHigh(thumbnail);

        var snippet = new ActivitySnippet()
                .setPublishedAt(new DateTime(Instant.parse(PUBLISHED_AT).toEpochMilli(), 0))
                .setChannelId("activity.snippet.channelId")
                .setTitle("activity.snippet.title")
                .setDescription("activity.snippet.description")
                .setThumbnails(thumbnails);

        var upload = new ActivityContentDetailsUpload()
                .setVideoId("activity.contentDetails.upload.videoId");

        var contentDetails = new ActivityContentDetails()
                .setUpload(upload);

        return new Activity()
                .setId("activity.id")
                .setSnippet(snippet)
                .setContentDetails(contentDetails);
    }
}

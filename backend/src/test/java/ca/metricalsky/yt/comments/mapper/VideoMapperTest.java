package ca.metricalsky.yt.comments.mapper;

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

public class VideoMapperTest {

    private static final String PUBLISHED_AT = "2025-01-01T00:00:00.000Z";

    private final VideoMapper videoMapper = new VideoMapperImpl();

    @Test
    void fromYouTube() {
        var ytActivity = buildYouTubeActivity();

        var video = videoMapper.fromYouTube(ytActivity);

        assertThat(video)
                .hasFieldOrPropertyWithValue("id", ytActivity.getContentDetails().getUpload().getVideoId())
                .hasFieldOrPropertyWithValue("publishedAt", OffsetDateTime.parse(PUBLISHED_AT))
                .hasFieldOrPropertyWithValue("channelId", ytActivity.getSnippet().getChannelId())
                .hasFieldOrPropertyWithValue("title", ytActivity.getSnippet().getTitle())
                .hasFieldOrPropertyWithValue("description", ytActivity.getSnippet().getDescription())
                .hasFieldOrPropertyWithValue("thumbnailUrl",
                        ytActivity.getSnippet().getThumbnails().getHigh().getUrl());
    }

    private static Activity buildYouTubeActivity() {
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

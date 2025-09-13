package ca.metricalsky.winston.convert.entity;

import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.entity.VideoRestrictionEntity.Restriction;
import ca.metricalsky.winston.test.UnitTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.ContentRating;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoContentDetailsRegionRestriction;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class YoutubeVideoToVideoDetailsEntityConverterTest {

    @InjectMocks
    private YoutubeVideoToVideoDetailsEntityConverter converter = new YoutubeVideoToVideoDetailsEntityConverterImpl();

    @Mock
    private ConversionServiceAdapter conversionServiceAdapter;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void convert() {
        var youtubeVideo = buildYoutubeVideo();

        var videoDetails = converter.convert(youtubeVideo);

        assertThat(videoDetails)
                .as("videoDetails")
                .isNotNull();

        assertThat(videoDetails.getRestrictions())
                .as("videoDetails.restrictions")
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("restriction", Restriction.ALLOWED)
                .hasFieldOrPropertyWithValue("country", "CA");

        assertThat(videoDetails.getContentRatings())
                .as("videoDetails.contentRatings")
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("authority", "catv")
                .hasFieldOrPropertyWithValue("rating", "catvG");
    }

    private Video buildYoutubeVideo() {
        var regionRestriction = new VideoContentDetailsRegionRestriction()
                .setAllowed(List.of("CA"));

        var contentRating = new ContentRating()
                .setCatvRating("catvG");

        var contentDetails = new VideoContentDetails()
                .setRegionRestriction(regionRestriction)
                .setContentRating(contentRating);

        return new Video()
                .setContentDetails(contentDetails);
    }
}

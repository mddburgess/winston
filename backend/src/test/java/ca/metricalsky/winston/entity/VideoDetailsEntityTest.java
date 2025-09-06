package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.entity.VideoDetailsEntity.Visibility;
import ca.metricalsky.winston.entity.VideoRestrictionEntity.Restriction;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VideoDetailsEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private String videoId;

    @BeforeEach
    void beforeEach() {
        var channelEntity = persistChannel();
        var videoEntity = persistVideo(channelEntity.getId());
        videoId = videoEntity.getId();
    }

    @Test
    void persistsWithAllOptionalFields() {
        var videoRestrictionEntity = VideoRestrictionEntity.builder()
                .videoId(videoId)
                .restriction(Restriction.ALLOWED)
                .country("CA")
                .build();

        var videoContentRatingEntity = VideoContentRatingEntity.builder()
                .videoId(videoId)
                .authority("catv")
                .rating("catvG")
                .build();

        var videoRecordingLocation =  VideoRecordingLocationEntity.builder()
                .videoId(videoId)
                .description("location")
                .latitude(45.4235937)
                .longitude(-75.700929)
                .altitude(1.0)
                .build();

        var videoDetailsEntity =  VideoDetailsEntity.builder()
                .videoId(videoId)
                .visibility(Visibility.PUBLIC)
                .duration(Duration.parse("PT6M27S"))
                .category("1")
                .topics(Set.of("topic"))
                .tags(Set.of("tag"))
                .restrictions(List.of(videoRestrictionEntity))
                .contentRatings(List.of(videoContentRatingEntity))
                .madeForKids(true)
                .containsSyntheticMedia(true)
                .hasPaidProductPlacement(true)
                .recordingLocation(videoRecordingLocation)
                .recordedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .liveStreamedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .viewCount(1L)
                .likeCount(2L)
                .commentCount(3L)
                .build();

        var persistedEntity = entityManager.persistFlushFind(videoDetailsEntity);

        assertThat(persistedEntity)
                .as("videoDetails")
                .hasFieldOrPropertyWithValue("videoId", videoDetailsEntity.getVideoId())
                .hasFieldOrPropertyWithValue("visibility", videoDetailsEntity.getVisibility())
                .hasFieldOrPropertyWithValue("duration", videoDetailsEntity.getDuration())
                .hasFieldOrPropertyWithValue("category", videoDetailsEntity.getCategory())
                .hasFieldOrPropertyWithValue("topics", videoDetailsEntity.getTopics())
                .hasFieldOrPropertyWithValue("tags", videoDetailsEntity.getTags())
                .hasFieldOrPropertyWithValue("madeForKids", videoDetailsEntity.getMadeForKids())
                .hasFieldOrPropertyWithValue("containsSyntheticMedia",
                        videoDetailsEntity.getContainsSyntheticMedia())
                .hasFieldOrPropertyWithValue("hasPaidProductPlacement",
                        videoDetailsEntity.getHasPaidProductPlacement())
                .hasFieldOrPropertyWithValue("recordedAt", videoDetailsEntity.getRecordedAt())
                .hasFieldOrPropertyWithValue("liveStreamedAt", videoDetailsEntity.getLiveStreamedAt())
                .hasFieldOrPropertyWithValue("viewCount", videoDetailsEntity.getViewCount())
                .hasFieldOrPropertyWithValue("likeCount", videoDetailsEntity.getLikeCount())
                .hasFieldOrPropertyWithValue("commentCount", videoDetailsEntity.getCommentCount());

        assertThat(persistedEntity.getRestrictions())
                .hasSize(1)
                .first()
                .as("videoDetails.restriction")
                .hasFieldOrPropertyWithValue("videoId", videoRestrictionEntity.getVideoId())
                .hasFieldOrPropertyWithValue("restriction", videoRestrictionEntity.getRestriction())
                .hasFieldOrPropertyWithValue("country", videoRestrictionEntity.getCountry());

        assertThat(persistedEntity.getContentRatings())
                .hasSize(1)
                .first()
                .as("videoDetails.contentRating")
                .hasFieldOrPropertyWithValue("videoId", videoContentRatingEntity.getVideoId())
                .hasFieldOrPropertyWithValue("authority",  videoContentRatingEntity.getAuthority())
                .hasFieldOrPropertyWithValue("rating", videoContentRatingEntity.getRating());

        assertThat(persistedEntity.getRecordingLocation())
                .isNotNull()
                .hasFieldOrPropertyWithValue("videoId", videoRecordingLocation.getVideoId())
                .hasFieldOrPropertyWithValue("description", videoRecordingLocation.getDescription())
                .hasFieldOrPropertyWithValue("latitude", videoRecordingLocation.getLatitude())
                .hasFieldOrPropertyWithValue("longitude", videoRecordingLocation.getLongitude())
                .hasFieldOrPropertyWithValue("altitude", videoRecordingLocation.getAltitude());
    }


    private ChannelEntity persistChannel() {
        var channelEntity = ChannelEntity.builder()
                .id(TestUtils.randomId())
                .build();
        return entityManager.persist(channelEntity);
    }

    private VideoEntity persistVideo(String channelId) {
        var videoEntity = VideoEntity.builder()
                .id(TestUtils.randomId())
                .channelId(channelId)
                .build();
        return entityManager.persist(videoEntity);
    }
}

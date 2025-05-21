package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VideoCommentsEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private VideoEntity videoEntity;

    @BeforeEach
    void beforeEach() {
        var channelEntity = persistChannel();
        videoEntity = persistVideo(channelEntity);
    }

    @Test
    void persists() {
        var videoCommentsEntity = VideoCommentsEntity.builder()
                .videoId(videoEntity.getId())
                .commentsDisabled(TestUtils.randomBoolean())
                .commentCount(TestUtils.randomLong())
                .replyCount(TestUtils.randomLong())
                .totalReplyCount(TestUtils.randomLong())
                .build();

        var persistedEntity = entityManager.persistFlushFind(videoCommentsEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("videoId", videoCommentsEntity.getVideoId())
                .hasFieldOrPropertyWithValue("commentsDisabled", videoCommentsEntity.isCommentsDisabled())
                .hasFieldOrPropertyWithValue("commentCount", videoCommentsEntity.getCommentCount())
                .hasFieldOrPropertyWithValue("replyCount", videoCommentsEntity.getReplyCount())
                .hasFieldOrPropertyWithValue("totalReplyCount", videoCommentsEntity.getTotalReplyCount())
                .hasNoNullFieldsOrProperties();
    }


    private ChannelEntity persistChannel() {
        var channelEntity = ChannelEntity.builder()
                .id(TestUtils.randomId())
                .build();
        return entityManager.persist(channelEntity);
    }

    private VideoEntity persistVideo(ChannelEntity channelEntity) {
        var videoEntity = VideoEntity.builder()
                .id(TestUtils.randomId())
                .channelId(channelEntity.getId())
                .build();
        return entityManager.persist(videoEntity);
    }
}

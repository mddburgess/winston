package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.entity.VideoEntity;
import ca.metricalsky.winston.entity.VideoCommentsEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = VideoCommentsRepository.class
))
class VideoCommentsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private VideoCommentsRepository repository;

    private VideoEntity video;

    @BeforeEach
    void beforeEach() {
        var channel = new ChannelEntity();
        channel.setId(RandomStringUtils.secure().nextAlphanumeric(10));
        channel = entityManager.persist(channel);

        video = new VideoEntity();
        video.setId(RandomStringUtils.secure().nextAlphanumeric(10));
        video.setChannelId(channel.getId());
        video = entityManager.persist(video);
    }

    @Test
    void save() {
        var videoComments = new VideoCommentsEntity();
        videoComments.setVideoId(video.getId());

        videoComments = repository.save(videoComments);
        entityManager.flush();

        assertThat(videoComments)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("commentsDisabled", false)
                .hasFieldOrPropertyWithValue("commentCount", 0L)
                .hasFieldOrPropertyWithValue("replyCount", 0L)
                .hasFieldOrPropertyWithValue("totalReplyCount", 0L);
    }
}

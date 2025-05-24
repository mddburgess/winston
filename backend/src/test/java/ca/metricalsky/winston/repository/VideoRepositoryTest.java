package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.entity.VideoEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = VideoRepository.class
))
class VideoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private VideoRepository repository;

    @Test
    void findAllByChannelHandle() {
        var channel = entityManager.persist(buildChannel());
        var video = entityManager.persist(buildVideo(channel));

        var videos = repository.findAllByChannelHandle(channel.getCustomUrl());

        assertThat(videos)
                .containsExactly(video);
    }

    @Test
    void findAllByChannelHandle_empty() {
        var channelHandle = RandomStringUtils.secure().nextAlphanumeric(10);

        var videos = repository.findAllByChannelHandle(channelHandle);

        assertThat(videos)
                .isEmpty();
    }

    private static ChannelEntity buildChannel() {
        var channel = new ChannelEntity();
        channel.setId(RandomStringUtils.secure().nextAlphanumeric(10));
        channel.setCustomUrl("@" + RandomStringUtils.secure().nextAlphanumeric(10));
        return channel;
    }

    private static VideoEntity buildVideo(ChannelEntity channel) {
        var video = new VideoEntity();
        video.setId(RandomStringUtils.secure().nextAlphanumeric(10));
        video.setChannelId(channel.getId());
        return video;
    }
}

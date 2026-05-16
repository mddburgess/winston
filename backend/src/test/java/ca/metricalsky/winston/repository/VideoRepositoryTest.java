package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.database.entity.video.VideoEntity;
import ca.metricalsky.winston.database.repository.video.VideoRepository;
import ca.metricalsky.winston.test.annotations.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
@Sql({
        "classpath:sql/channels.sql",
        "classpath:sql/videos.sql"
})
class VideoRepositoryTest {

    private static final String CHANNEL_ID = "channelId";
    private static final String CHANNEL_ID_NOT_FOUND = "notFound";

    @Autowired
    private VideoRepository repository;

    @Test
    void findPageByChannelId() {
        var page = PageRequest.of(0, 1);

        var result = repository.findPageByChannelId(CHANNEL_ID, page);

        assertThat(result)
                .extracting(VideoEntity::getId)
                .containsExactly("videoId1");
    }

    @Test
    void findPageByChannelId_notFound() {
        var page = PageRequest.of(0, 1);

        var result = repository.findPageByChannelId(CHANNEL_ID_NOT_FOUND, page);

        assertThat(result)
                .isEmpty();
    }
}

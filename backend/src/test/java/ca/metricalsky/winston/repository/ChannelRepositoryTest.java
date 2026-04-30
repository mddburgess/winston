package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.database.repository.channel.ChannelRepository;
import ca.metricalsky.winston.test.annotations.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
@Sql("classpath:sql/channels.sql")
class ChannelRepositoryTest {

    private static final String CHANNEL_ID = "channelId";
    private static final String CUSTOM_URL = "@customUrl";
    private static final String CUSTOM_URL_NOT_FOUND = "@notFound";

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    void findIdByCustomUrl() {
        var result = channelRepository.findIdByCustomUrl(CUSTOM_URL);

        assertThat(result)
                .hasValue(CHANNEL_ID);
    }

    @Test
    void findIdByCustomUrl_notFound() {
        var result = channelRepository.findIdByCustomUrl(CUSTOM_URL_NOT_FOUND);

        assertThat(result)
                .isEmpty();
    }
}

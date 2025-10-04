package ca.metricalsky.winston.config.youtube;

import ca.metricalsky.winston.config.properties.youtube.YouTubeConfig;
import ca.metricalsky.winston.test.annotations.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class YouTubeConfigTest {

    @Autowired
    private YouTubeConfig youTubeConfig;

    @Test
    void getApiKey() {
        assertThat(youTubeConfig.getApiKey())
                .isEqualTo("YOUTUBE_API_KEY");
    }

    @Test
    void getDailyRequestQuota() {
        assertThat(youTubeConfig.getDailyRequestQuota())
                .isEqualTo(10000);
    }

    @Test
    void getMockRootUrl() {
        assertThat(youTubeConfig.getMockRootUrl())
                .matches("http://localhost:\\d+");
    }
}

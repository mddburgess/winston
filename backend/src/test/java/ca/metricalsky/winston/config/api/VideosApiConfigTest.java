package ca.metricalsky.winston.config.api;

import ca.metricalsky.winston.config.properties.api.VideosApiConfig;
import ca.metricalsky.winston.test.annotations.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class VideosApiConfigTest {

    @Autowired
    private VideosApiConfig videosApiConfig;

    @Test
    void getDefaultPageSize() {
        assertThat(videosApiConfig.getDefaultPageSize())
                .isEqualTo(10);
    }
}

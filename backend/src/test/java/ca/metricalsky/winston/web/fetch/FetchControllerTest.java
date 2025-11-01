package ca.metricalsky.winston.web.fetch;

import ca.metricalsky.winston.config.properties.youtube.YouTubeConfig;
import ca.metricalsky.winston.service.fetch.FetchService;
import ca.metricalsky.winston.test.annotations.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(FetchController.class)
@EnableConfigurationProperties(YouTubeConfig.class)
class FetchControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private FetchService fetchService;

    @Test
    void getFetchLimits() throws Exception {
        when(fetchService.getAvailableQuota())
                .thenReturn(5000);

        mvc.perform(get("/api/v1/fetch/limits")).andExpectAll(
                status().isOk(),
                jsonPath("$.daily_quota").value(10000),
                jsonPath("$.available_quota").value(5000)
        );
    }
}

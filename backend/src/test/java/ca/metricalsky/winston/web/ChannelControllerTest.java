package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.service.ChannelService;
import ca.metricalsky.winston.test.annotations.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ChannelController.class)
class ChannelControllerTest {

    private static final String CHANNEL_HANDLE = "@channelHandle";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ChannelDataService channelDataService;
    @MockitoBean
    private ChannelService channelService;

    @Test
    void list() throws Exception {
        when(channelService.getAllChannels(false))
                .thenReturn(List.of(buildChannel()));

        mvc.perform(get("/api/v1/channels")).andExpectAll(
                status().isOk(),
                jsonPath("$.channels", hasSize(1)),
                jsonPath("$.channels[0].id").value("channel.id"),
                jsonPath("$.channels[0].title").value("channel.title"),
                jsonPath("$.channels[0].description").value("channel.description"),
                jsonPath("$.channels[0].handle").value(CHANNEL_HANDLE)
        );
    }

    @Test
    void list_noResults() throws Exception {
        when(channelService.getAllChannels(false))
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/channels")).andExpectAll(
                status().isOk(),
                jsonPath("$.channels", hasSize(0))
        );
    }

    @Test
    void findByHandle() throws Exception {
        when(channelService.getChannelByHandle(CHANNEL_HANDLE))
                .thenReturn(buildChannel());

        mvc.perform(get("/api/v1/channels/{handle}", CHANNEL_HANDLE)).andExpectAll(
                status().isOk(),
                jsonPath("$.id").value("channel.id"),
                jsonPath("$.title").value("channel.title"),
                jsonPath("$.description").value("channel.description"),
                jsonPath("$.handle").value(CHANNEL_HANDLE)
        );
    }

    @Test
    void findByHandle_notFound() throws Exception {
        when(channelService.getChannelByHandle(CHANNEL_HANDLE))
                .thenThrow(new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        mvc.perform(get("/api/v1/channels/{channelHandle}", CHANNEL_HANDLE)).andExpectAll(
                status().isNotFound(),
                jsonPath("$.title").value("Not Found"),
                jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                jsonPath("$.detail").value("The requested channel was not found.")
        );
    }

    private static Channel buildChannel() {
        return new Channel()
                .id("channel.id")
                .title("channel.title")
                .description("channel.description")
                .handle(CHANNEL_HANDLE);
    }
}

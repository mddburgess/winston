package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.config.AppResourceResolver;
import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.service.ChannelService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@ExtendWith(SpringExtension.class)
@WebMvcTest({ChannelController.class, AppResourceResolver.class, ConversionServiceAdapter.class})
class ChannelControllerTest {

    private static final String CHANNEL_HANDLE = "@channelHandle";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ChannelService channelService;

    @Test
    void list() throws Exception {
        when(channelService.getAllChannels())
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
        when(channelService.getAllChannels())
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

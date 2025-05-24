package ca.metricalsky.winston.web;

import ca.metricalsky.winston.config.AppResourceResolver;
import ca.metricalsky.winston.dto.ChannelDto;
import ca.metricalsky.winston.service.ChannelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest({ChannelController.class, AppResourceResolver.class})
class ChannelControllerTest {

    private static final String CHANNEL_HANDLE = "@channelHandle";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ChannelService channelService;

    @Test
    void list() throws Exception{
        when(channelService.findAll())
                .thenReturn(List.of(buildChannelDto()));

        mvc.perform(get("/api/v1/channels")).andExpectAll(
                status().isOk(),
                jsonPath("$", hasSize(1)),
                jsonPath("$[0].id").value("channel.id"),
                jsonPath("$[0].title").value("channel.title"),
                jsonPath("$[0].description").value("channel.description"),
                jsonPath("$[0].customUrl").value(CHANNEL_HANDLE)
        );
    }

    @Test
    void list_noResults() throws Exception {
        when(channelService.findAll())
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/channels")).andExpectAll(
                status().isOk(),
                jsonPath("$", hasSize(0))
        );
    }

    @Test
    void findByHandle() throws Exception {
        when(channelService.findByHandle(CHANNEL_HANDLE))
                .thenReturn(Optional.of(buildChannelDto()));

        mvc.perform(get("/api/v1/channels/{channelHandle}", CHANNEL_HANDLE)).andExpectAll(
                status().isOk(),
                jsonPath("$.id").value("channel.id"),
                jsonPath("$.title").value("channel.title"),
                jsonPath("$.description").value("channel.description"),
                jsonPath("$.customUrl").value(CHANNEL_HANDLE)
        );
    }

    @Test
    void findByHandle_notFound() throws Exception {
        when(channelService.findByHandle(CHANNEL_HANDLE))
                .thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/channels/{channelHandle}", CHANNEL_HANDLE)).andExpectAll(
                status().isNotFound(),
                jsonPath("$.title").value("Not Found"),
                jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                jsonPath("$.detail").value("The requested channel was not found.")
        );
    }

    private static ChannelDto buildChannelDto() {
        var channelDto = new ChannelDto();
        channelDto.setId("channel.id");
        channelDto.setTitle("channel.title");
        channelDto.setDescription("channel.description");
        channelDto.setCustomUrl(CHANNEL_HANDLE);
        return channelDto;
    }
}

package ca.metricalsky.winston.web;

import ca.metricalsky.winston.config.properties.api.VideosApiConfig;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.test.annotations.ControllerTest;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(VideoController.class)
class VideoControllerTest {

    private static final WinstonFaker faker = new WinstonFaker();

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ChannelDataService channelDataService;
    @MockitoBean
    private VideoDataService videoDataService;
    @MockitoBean
    private VideosApiConfig videosApiConfig;

    @Test
    void listVideos() throws Exception {
        var channelHandle = faker.youtube().channelHandle();
        var channelId = faker.youtube().channelId();
        var video = faker.video().dto(channelHandle);

        when(videosApiConfig.getDefaultPageSize())
                .thenReturn(10);
        when(channelDataService.findChannelIdByHandle(channelHandle))
                .thenReturn(Optional.of(channelId));
        when(videoDataService.countByChannelId(channelId))
                .thenReturn(1);
        when(videoDataService.listVideosByChannelId(channelId, PageRequest.of(0, 10)))
                .thenReturn(List.of(video));

        mvc.perform(get("/api/v1/channels/{handle}/videos", channelHandle)).andExpectAll(
                status().isOk(),
                jsonPath("$.channel_handle").value(channelHandle),
                jsonPath("$.results.page_count").value(1),
                jsonPath("$.results.total_count").value(1),
                jsonPath("$.videos", hasSize(1)),
                jsonPath("$.videos[0].id").value(video.getId())
        );
    }

    @Test
    void listVideos_noResults() throws Exception {
        var channelHandle = faker.youtube().channelHandle();
        var channelId = faker.youtube().channelId();

        when(videosApiConfig.getDefaultPageSize())
                .thenReturn(10);
        when(channelDataService.findChannelIdByHandle(channelHandle))
                .thenReturn(Optional.of(channelId));
        when(videoDataService.countByChannelId(channelId))
                .thenReturn(0);
        when(videoDataService.listVideosByChannelId(channelId, PageRequest.of(0, 10)))
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/channels/{handle}/videos", channelHandle)).andExpectAll(
                status().isOk(),
                jsonPath("$.channel_handle").value(channelHandle),
                jsonPath("$.results.page_count").value(0),
                jsonPath("$.results.total_count").value(0),
                jsonPath("$.videos", hasSize(0))
        );
    }

    @Test
    void listVideos_channelNotFound() throws Exception {
        var channelHandle = faker.youtube().channelHandle();

        when(videosApiConfig.getDefaultPageSize())
                .thenReturn(10);
        when(channelDataService.findChannelIdByHandle(channelHandle))
                .thenThrow(new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        mvc.perform(get("/api/v1/channels/{handle}/videos", channelHandle)).andExpectAll(
                status().isNotFound(),
                jsonPath("$.title").value(HttpStatus.NOT_FOUND.getReasonPhrase()),
                jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                jsonPath("$.detail").value(ErrorCode.CHANNEL_NOT_FOUND.getDetail())
        );

        verifyNoInteractions(videoDataService);
    }

    @Test
    void getVideo() throws Exception {
        var video = faker.video().dto();

        when(videoDataService.findVideoById(video.getId()))
                .thenReturn(Optional.of(video));

        mvc.perform(get("/api/v1/videos/{id}", video.getId())).andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(video.getId())
        );
    }

    @Test
    void getVideo_notFound() throws Exception {
        var videoId = faker.youtube().videoId();

        when(videoDataService.findVideoById(videoId))
                .thenThrow(new AppException(ErrorCode.VIDEO_NOT_FOUND));

        mvc.perform(get("/api/v1/videos/{id}", videoId)).andExpectAll(
                status().isNotFound(),
                jsonPath("$.title").value(HttpStatus.NOT_FOUND.getReasonPhrase()),
                jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                jsonPath("$.detail").value(ErrorCode.VIDEO_NOT_FOUND.getDetail())
        );
    }
}

package ca.metricalsky.winston.service;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.database.repository.channel.ChannelRepository;
import ca.metricalsky.winston.test.TestUtils;
import ca.metricalsky.winston.test.annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@UnitTest
class ChannelServiceTest {

    private static final String CHANNEL_ID = "channelId";
    private static final String CHANNEL_HANDLE = "channelHandle";

    @InjectMocks
    private ChannelService channelService;

    @Mock
    private ChannelDataService channelDataService;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private VideoDataService videoDataService;

    @Test
    void getAllChannels() {
        var channel = buildChannel();

        when(channelDataService.getAllChannels(false))
                .thenReturn(List.of(channel));
        when(videoDataService.countAllVideosByChannelId())
                .thenReturn(Map.of(channel.getId(), 1));

        var channels = channelService.getAllChannels(false);

        assertThat(channels).first()
                .isSameAs(channel);
        assertThat(channel.getVideoCount())
                .isEqualTo(1);
    }

    @Test
    void getAllChannels_empty() {
        when(channelDataService.getAllChannels(false))
                .thenReturn(List.of());
        when(videoDataService.countAllVideosByChannelId())
                .thenReturn(Map.of());

        var channels = channelService.getAllChannels(false);

        assertThat(channels)
                .isEmpty();
    }

    @Test
    void getChannelByHandle() {
        var channel = buildChannel();
        when(channelDataService.findChannelByHandle(CHANNEL_HANDLE))
                .thenReturn(Optional.of(channel));

        var videoCount = 1;
        when(videoDataService.countByChannelId(channel.getId()))
                .thenReturn(videoCount);

        var result = channelService.getChannelByHandle(CHANNEL_HANDLE);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("videoCount", videoCount);
    }

    @Test
    void getChannelByHandle_notFound() {
        var exception = new AppException(ErrorCode.CHANNEL_NOT_FOUND);
        when(channelDataService.findChannelByHandle(CHANNEL_HANDLE))
                .thenThrow(exception);

        assertThatThrownBy(() -> channelService.getChannelByHandle(CHANNEL_HANDLE))
                .isEqualTo(exception);
    }

    @Test
    void requireChannelExists_channelExists() {
        when(channelRepository.existsById(CHANNEL_ID))
                .thenReturn(true);

        assertThatCode(() -> channelService.requireChannelExists(CHANNEL_ID))
                .doesNotThrowAnyException();
    }

    @Test
    void requireChannelExists_channelDoesNotExist() {
        when(channelRepository.existsById(CHANNEL_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> channelService.requireChannelExists(CHANNEL_ID))
                .isExactlyInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                .hasMessageEndingWith("The requested channel was not found.");
    }

    private static Channel buildChannel() {
        return new Channel()
                .id(TestUtils.randomId())
                .handle(TestUtils.randomString());
    }
}

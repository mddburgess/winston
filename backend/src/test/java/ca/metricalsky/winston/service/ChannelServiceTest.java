package ca.metricalsky.winston.service;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.ChannelRepository;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

    private static final String CHANNEL_ID = "channelId";

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

        when(channelDataService.getAllChannels())
                .thenReturn(List.of(channel));
        when(videoDataService.countAllVideosByChannelId())
                .thenReturn(Map.of(channel.getId(), 1));

        var channels = channelService.getAllChannels();

        assertThat(channels).first()
                .isSameAs(channel);
        assertThat(channel.getVideoCount())
                .isEqualTo(1);
    }

    @Test
    void getAllChannels_empty() {
        when(channelDataService.getAllChannels())
                .thenReturn(List.of());
        when(videoDataService.countAllVideosByChannelId())
                .thenReturn(Map.of());

        var channels = channelService.getAllChannels();

        assertThat(channels)
                .isEmpty();
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

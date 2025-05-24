package ca.metricalsky.winston.service;

import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.ChannelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

    private static final String CHANNEL_ID = "channelId";
    private static final String CHANNEL_HANDLE = "channelHandle";

    @InjectMocks
    private ChannelService channelService;

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private VideoService videoService;

    @Test
    void findAll() {
        when(videoService.countAllByChannelId())
                .thenReturn(Map.of(CHANNEL_ID, 1L));
        when(channelRepository.findAll())
                .thenReturn(List.of(buildChannel()));

        var channelDtos = channelService.findAll();

        assertThat(channelDtos)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("id", CHANNEL_ID)
                .hasFieldOrPropertyWithValue("customUrl", CHANNEL_HANDLE)
                .hasFieldOrPropertyWithValue("videoCount", 1L);
    }

    @Test
    void findAll_empty() {
        when(videoService.countAllByChannelId())
                .thenReturn(Map.of());
        when(channelRepository.findAll())
                .thenReturn(List.of());

        var channelDtos = channelService.findAll();

        assertThat(channelDtos)
                .isEmpty();
    }

    @Test
    void findByHandle() {
        when(channelRepository.findByCustomUrl(CHANNEL_HANDLE))
                .thenReturn(Optional.of(buildChannel()));

        var channelDto = channelService.findByHandle(CHANNEL_HANDLE);

        assertThat(channelDto).get()
                .hasFieldOrPropertyWithValue("id", CHANNEL_ID)
                .hasFieldOrPropertyWithValue("customUrl", CHANNEL_HANDLE);
    }

    @Test
    void findByHandle_notFound() {
        when(channelRepository.findByCustomUrl(CHANNEL_HANDLE))
                .thenReturn(Optional.empty());

        var channelDto = channelService.findByHandle(CHANNEL_HANDLE);

        assertThat(channelDto)
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

    private static ChannelEntity buildChannel() {
        var channel = new ChannelEntity();
        channel.setId(CHANNEL_ID);
        channel.setCustomUrl(CHANNEL_HANDLE);
        return channel;
    }
}

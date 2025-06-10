package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.mappers.api.ChannelMapper;
import ca.metricalsky.winston.mappers.api.ChannelMapperImpl;
import ca.metricalsky.winston.repository.ChannelRepository;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChannelDataServiceTest {

    @InjectMocks
    private ChannelDataService channelDataService;

    @Spy
    private ChannelMapper channelMapper = new ChannelMapperImpl();
    @Mock
    private ChannelRepository channelRepository;

    @Test
    void getAllChannels() {
        var channelEntity = buildChannel();

        when(channelRepository.findAll())
                .thenReturn(List.of(channelEntity));

        var channels = channelDataService.getAllChannels();

        assertThat(channels).first()
                .hasFieldOrPropertyWithValue("id", channelEntity.getId())
                .hasFieldOrPropertyWithValue("handle", channelEntity.getCustomUrl());
    }

    @Test
    void getAllChannels_empty() {
        when(channelRepository.findAll())
                .thenReturn(List.of());

        var channels = channelDataService.getAllChannels();

        assertThat(channels)
                .isEmpty();
    }

    @Test
    void findChannelByHandle() {
        var channelEntity = buildChannel();

        when(channelRepository.findByCustomUrl(channelEntity.getCustomUrl()))
                .thenReturn(Optional.of(channelEntity));

        var channel = channelDataService.findChannelByHandle(channelEntity.getCustomUrl());

        assertThat(channel).get()
                .hasFieldOrPropertyWithValue("id", channelEntity.getId())
                .hasFieldOrPropertyWithValue("handle", channelEntity.getCustomUrl());
    }

    @Test
    void findChannelByHandle_notFound() {
        var channelHandle = TestUtils.randomString();

        when(channelRepository.findByCustomUrl(channelHandle))
                .thenReturn(Optional.empty());

        var channel = channelDataService.findChannelByHandle(channelHandle);

        assertThat(channel)
                .isEmpty();
    }

    private static ChannelEntity buildChannel() {
        return ChannelEntity.builder()
                .id(TestUtils.randomId())
                .customUrl(TestUtils.randomString())
                .build();
    }
}

package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.database.repository.channel.ChannelRepository;
import ca.metricalsky.winston.test.TestUtils;
import ca.metricalsky.winston.test.annotations.UnitTest;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@UnitTest
class ChannelDataServiceTest {

    private static final WinstonFaker faker = new WinstonFaker();

    @InjectMocks
    private ChannelDataService channelDataService;

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ConversionService conversionService;

    @Test
    void getAllChannels() {
        var channelEntity = faker.database().channel().completeEntity();
        var channel = new Channel();

        when(channelRepository.findAll())
                .thenReturn(List.of(channelEntity));
        when(conversionService.convert(channelEntity, Channel.class))
                .thenReturn(channel);

        var channels = channelDataService.getAllChannels(true);

        assertThat(channels).first()
                .isSameAs(channel);
    }

    @Test
    void getAllChannels_empty() {
        when(channelRepository.findAll())
                .thenReturn(List.of());

        var channels = channelDataService.getAllChannels(true);

        assertThat(channels)
                .isEmpty();
    }

    @Test
    void findChannelIdByHandle() {
        var channelHandle = faker.youtube().channelHandle();
        var channelId = faker.youtube().channelId();

        when(channelRepository.findIdByCustomUrl(channelHandle))
                .thenReturn(channelId);

        var result = channelDataService.findChannelIdByHandle(channelHandle);

        assertThat(result)
                .hasValue(channelId);
    }

    @Test
    void findChannelIdByHandle_notFound() {
        var channelHandle = faker.youtube().channelHandle();

        when(channelRepository.findIdByCustomUrl(channelHandle))
                .thenReturn(null);

        var result = channelDataService.findChannelIdByHandle(channelHandle);

        assertThat(result)
                .isEmpty();
    }

    @Test
    void findChannelByHandle() {
        var channelEntity = faker.database().channel().completeEntity();
        var channel = new Channel();

        when(channelRepository.findByCustomUrl(channelEntity.getCustomUrl()))
                .thenReturn(Optional.of(channelEntity));
        when(conversionService.convert(channelEntity, Channel.class))
                .thenReturn(channel);

        var result = channelDataService.findChannelByHandle(channelEntity.getCustomUrl());

        assertThat(result)
                .hasValue(channel);
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
}

package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.repository.ChannelRepository;
import ca.metricalsky.winston.test.TestUtils;
import ca.metricalsky.winston.test.annotations.UnitTest;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;

import static ca.metricalsky.winston.test.factory.entity.ChannelEntityFactory.createChannelEntity;
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
    @Disabled
    void getAllChannels() {
        var channelEntity = createChannelEntity();

        when(channelRepository.findAll())
                .thenReturn(List.of(channelEntity));

        var channels = channelDataService.getAllChannels(true);

        assertThat(channels).first()
                .hasFieldOrPropertyWithValue("id", channelEntity.getId())
                .hasFieldOrPropertyWithValue("handle", channelEntity.getCustomUrl());
    }

    @Test
    @Disabled
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
                .thenReturn(Optional.of(channelId));

        var result = channelDataService.findChannelIdByHandle(channelHandle);

        assertThat(result)
                .hasValue(channelId);
    }

    @Test
    void findChannelIdByHandle_notFound() {
        var channelHandle = faker.youtube().channelHandle();

        when(channelRepository.findIdByCustomUrl(channelHandle))
                .thenReturn(Optional.empty());

        var result = channelDataService.findChannelIdByHandle(channelHandle);

        assertThat(result)
                .isEmpty();
    }

    @Test
    @Disabled
    void findChannelByHandle() {
        var channelEntity = createChannelEntity();

        when(channelRepository.findByCustomUrl(channelEntity.getCustomUrl()))
                .thenReturn(Optional.of(channelEntity));

        var channel = channelDataService.findChannelByHandle(channelEntity.getCustomUrl());

        assertThat(channel).get()
                .hasFieldOrPropertyWithValue("id", channelEntity.getId())
                .hasFieldOrPropertyWithValue("handle", channelEntity.getCustomUrl());
    }

    @Test
    @Disabled
    void findChannelByHandle_notFound() {
        var channelHandle = TestUtils.randomString();

        when(channelRepository.findByCustomUrl(channelHandle))
                .thenReturn(Optional.empty());

        var channel = channelDataService.findChannelByHandle(channelHandle);

        assertThat(channel)
                .isEmpty();
    }
}

package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchChannelActionTest {

    private static final WinstonFaker faker = new WinstonFaker();

    @InjectMocks
    private FetchChannelAction fetchChannelAction;

    @Mock
    private ChannelDataService channelDataService;
    @Mock
    private YouTubeService youTubeService;

    private FetchActionEntity fetchAction;

    @BeforeEach
    void beforeEach() {
        var fetchRequest = faker.database().fetchRequest().minimalEntity();
        var fetchOperation = faker.database().fetchOperation().channels(fetchRequest);
        fetchAction = faker.database().fetchAction().channels(fetchOperation);
    }

    @Test
    void fetch() {
        var channelListResponse = faker.youtube().response().channelList().channelFound();
        when(youTubeService.getChannels(fetchAction))
                .thenReturn(channelListResponse);

        var channel = new Channel();
        when(channelDataService.saveChannel(channelListResponse))
                .thenReturn(Optional.of(channel));

        var fetchResult = fetchChannelAction.fetch(fetchAction);

        assertThat(fetchResult)
                .isNotNull()
                .hasFieldOrPropertyWithValue("actionType", fetchAction.getActionType())
                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                .hasFieldOrPropertyWithValue("items", List.of(channel))
                .hasFieldOrPropertyWithValue("nextFetchAction", null);
    }

    @Test
    void fetch_notFound() {
        var channelListResponse = faker.youtube().response().channelList().emptyPage();
        when(youTubeService.getChannels(fetchAction))
                .thenReturn(channelListResponse);

        when(channelDataService.saveChannel(channelListResponse))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> fetchChannelAction.fetch(fetchAction))
                .isExactlyInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                .hasMessageEndingWith("The requested channel was not found.");
    }
}

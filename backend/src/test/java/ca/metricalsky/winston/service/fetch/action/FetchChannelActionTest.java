package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity.Type;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.test.ClientTestObjectFactory;
import ca.metricalsky.winston.test.TestUtils;
import com.google.api.services.youtube.model.ChannelListResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchChannelActionTest {

    @InjectMocks
    private FetchChannelAction fetchChannelAction;

    @Mock
    private FetchActionService fetchActionService;
    @Mock
    private ChannelDataService channelDataService;
    @Mock
    private YouTubeService youTubeService;
    @Mock
    private SsePublisher ssePublisher;

    @Test
    @Disabled
    void fetch() {
        var fetchAction = FetchActionEntity.builder()
                .actionType(Type.CHANNELS)
                .objectId(TestUtils.randomId())
                .build();
        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);

        var channelListResponse = ClientTestObjectFactory.buildChannelListResponse();
        when(youTubeService.getChannels(fetchAction))
                .thenReturn(channelListResponse);

        var channel = new Channel();
        when(channelDataService.saveChannel(channelListResponse))
                .thenReturn(Optional.of(channel));

        var nextFetchAction = fetchChannelAction.fetch(fetchAction);

        assertThat(nextFetchAction)
                .as("nextFetchAction")
                .isNull();

        verify(fetchActionService).actionSuccessful(fetchAction, channelListResponse.getItems().size());
    }

    @Test
    @Disabled
    void fetch_notFound() {
        var fetchAction = FetchActionEntity.builder()
                .actionType(Type.CHANNELS)
                .objectId(TestUtils.randomId())
                .build();
        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);

        var channelListResponse = new ChannelListResponse();
        when(youTubeService.getChannels(fetchAction))
                .thenReturn(channelListResponse);

        when(channelDataService.saveChannel(channelListResponse))
                .thenReturn(Optional.empty());

        var appException = catchThrowableOfType(AppException.class,
                () -> fetchChannelAction.fetch(fetchAction));

        assertThat(appException)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                .hasMessageEndingWith("The requested channel does not exist.");

        verify(fetchActionService).actionFailed(fetchAction, appException);
        verifyNoInteractions(ssePublisher);
    }
}

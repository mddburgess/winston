package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.entity.Channel;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchAction.ActionType;
import ca.metricalsky.winston.events.FetchDataEvent;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.ChannelRepository;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import com.google.api.services.youtube.model.ChannelListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchChannelActionHandlerTest {

    private static final String CHANNEL_HANDLE = "channelHandle";

    @InjectMocks
    private FetchChannelActionHandler fetchChannelActionHandler;

    @Mock
    private FetchActionService fetchActionService;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private YouTubeClientAdapter youTubeClientAdapter;
    @Mock
    private SsePublisher ssePublisher;
    @Captor
    private ArgumentCaptor<FetchDataEvent> fetchDataEvent;

    @Test
    void fetch() {
        var fetchAction = FetchAction.builder()
                .actionType(ActionType.CHANNELS)
                .objectId(CHANNEL_HANDLE)
                .build();
        var channelListResponse = new ChannelListResponse();
        channelListResponse.setItems(List.of(new com.google.api.services.youtube.model.Channel()));

        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);
        when(youTubeClientAdapter.getChannels(fetchAction))
                .thenReturn(channelListResponse);

        var nextFetchAction = fetchChannelActionHandler.fetch(fetchAction, ssePublisher);

        assertThat(nextFetchAction).isNull();

        verify(channelRepository).save(any(Channel.class));
        verify(fetchActionService).actionCompleted(fetchAction, channelListResponse.getItems().size());
        verify(ssePublisher).publish(fetchDataEvent.capture());

        assertThat(fetchDataEvent.getValue())
                .hasFieldOrPropertyWithValue("objectId", CHANNEL_HANDLE);
        assertThat(fetchDataEvent.getValue().items()).hasSize(channelListResponse.getItems().size());
    }

    @Test
    void fetch_notFound() {
        var fetchAction = FetchAction.builder()
                .actionType(ActionType.CHANNELS)
                .objectId(CHANNEL_HANDLE)
                .build();
        var channelListResponse = new ChannelListResponse();

        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);
        when(youTubeClientAdapter.getChannels(fetchAction))
                .thenReturn(channelListResponse);

        var appException = catchThrowableOfType(AppException.class,
                () -> fetchChannelActionHandler.fetch(fetchAction, ssePublisher));

        assertThat(appException)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                .hasMessageEndingWith("The requested channel does not exist.");

        verify(fetchActionService).actionFailed(fetchAction, appException);
        verifyNoInteractions(ssePublisher);
    }
}

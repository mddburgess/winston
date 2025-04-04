package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.entity.Channel;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.repository.ChannelRepository;
import com.google.api.services.youtube.model.ChannelListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private ChannelRepository channelRepository;
    @Mock
    private YouTubeClientAdapter youTubeClientAdapter;

    @Test
    void fetch() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.CHANNELS)
                .objectId(CHANNEL_HANDLE)
                .build();
        var channelListResponse = new ChannelListResponse();
        channelListResponse.setItems(List.of(new com.google.api.services.youtube.model.Channel()));

        when(youTubeClientAdapter.getChannels(fetchAction)).thenReturn(channelListResponse);

        var fetchResult = fetchChannelActionHandler.fetch(fetchAction);

        assertThat(fetchResult)
                .hasFieldOrPropertyWithValue("actionType", FetchAction.ActionType.CHANNELS)
                .hasFieldOrPropertyWithValue("objectId", CHANNEL_HANDLE)
                .hasFieldOrPropertyWithValue("nextFetchAction", null);
        assertThat(fetchResult.items())
                .hasSize(1);

        verify(channelRepository).save(any(Channel.class));
    }

    @Test
    void fetch_notFound() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.CHANNELS)
                .objectId(CHANNEL_HANDLE)
                .build();
        var channelListResponse = new ChannelListResponse();

        when(youTubeClientAdapter.getChannels(fetchAction)).thenReturn(channelListResponse);

        assertThatThrownBy(() -> fetchChannelActionHandler.fetch(fetchAction))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);

        verifyNoInteractions(channelRepository);
    }
}

package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.events.FetchDataEvent;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import ca.metricalsky.winston.test.ClientTestObjectFactory;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FetchVideosActionHandlerTest {

    @InjectMocks
    private FetchVideosActionHandler fetchVideosActionHandler;

    @Mock
    private FetchActionService fetchActionService;
    @Mock
    private VideoDataService videoDataService;
    @Mock
    private YouTubeService youTubeService;
    @Mock
    private SsePublisher ssePublisher;
    @Captor
    private ArgumentCaptor<FetchDataEvent> fetchDataEvent;

    @Test
    @Disabled
    void fetch() {
        var fetchAction = FetchActionEntity.builder()
                .actionType(FetchActionEntity.Type.VIDEOS)
                .objectId(TestUtils.randomId())
                .build();
        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);

        var activityListResponse = ClientTestObjectFactory.buildActivityListResponse();
        when(youTubeService.getActivities(fetchAction))
                .thenReturn(activityListResponse);

        var video = new Video();
        when(videoDataService.saveVideos(activityListResponse))
                .thenReturn(List.of(video));

        doCallRealMethod()
                .when(ssePublisher).publish(any(FetchResult.class));

        var nextFetchAction = fetchVideosActionHandler.fetch(fetchAction);

        assertThat(nextFetchAction)
                .as("nextFetchAction")
                .isNull();

        verify(fetchActionService).actionSuccessful(fetchAction, 1);
        verify(ssePublisher).publish(fetchDataEvent.capture());

        assertThat(fetchDataEvent.getValue())
                .as("fetchDataEvent")
                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId());
        assertThat(fetchDataEvent.getValue().items())
                .as("fetchDataEvent.items")
                .hasSize(1)
                .first().isEqualTo(video);
    }
}

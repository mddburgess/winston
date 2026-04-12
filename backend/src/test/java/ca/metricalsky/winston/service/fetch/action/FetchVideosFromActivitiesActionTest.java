package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchVideosFromActivitiesActionTest {

    private static final WinstonFaker faker = new WinstonFaker();

    @InjectMocks
    private FetchVideosFromActivitiesAction fetchVideosFromActivitiesAction;

    @Mock
    private ChannelDataService channelDataService;
    @Mock
    private VideoDataService videoDataService;
    @Mock
    private YouTubeService youTubeService;

    @Test
    void fetch_emptyResponse() {
        var fetchAction = FetchActionEntity.builder()
                .actionType(FetchActionEntity.Type.VIDEOS)
                .objectId(faker.youtube().channelId())
                .build();

        var activityListResponse = faker.youtube().response().activityList().emptyPage();
        when(youTubeService.getActivities(fetchAction))
                .thenReturn(activityListResponse);

        var fetchResult = fetchVideosFromActivitiesAction.fetch(fetchAction);

        assertThat(fetchResult)
                .as("fetchResult")
                .hasFieldOrPropertyWithValue("actionType", fetchAction.getActionType())
                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                .hasFieldOrPropertyWithValue("items", Collections.emptyList())
                .hasFieldOrPropertyWithValue("nextFetchAction", null);

        verify(youTubeService, never()).getVideos(any(), any());
        verifyNoInteractions(channelDataService, videoDataService);
    }
}

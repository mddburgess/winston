package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.events.FetchDataEvent;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityContentDetails;
import com.google.api.services.youtube.model.ActivityContentDetailsPlaylistItem;
import com.google.api.services.youtube.model.ActivityContentDetailsUpload;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ActivitySnippet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FetchVideosActionHandlerTest {

    private static final String CHANNEL_ID = "channelId";
    private static final String VIDEO_ID = "videoId";
    private static final String PLAYLIST_ID = "playlistId";

    @InjectMocks
    private FetchVideosActionHandler fetchVideosActionHandler;

    @Mock
    private FetchActionService fetchActionService;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private YouTubeService youTubeService;
    @Mock
    private SsePublisher ssePublisher;
    @Captor
    private ArgumentCaptor<FetchDataEvent> fetchDataEvent;

    @Test
    void fetch() {
        var fetchAction = FetchActionEntity.builder()
                .actionType(FetchActionEntity.Type.VIDEOS)
                .objectId(CHANNEL_ID)
                .build();
        var activityListResponse = new ActivityListResponse();
        activityListResponse.setItems(List.of(buildUploadActivity(), buildPlaylistItemActivity()));

        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);
        when(youTubeService.getActivities(fetchAction))
                .thenReturn(activityListResponse);

        var nextFetchAction = fetchVideosActionHandler.fetch(fetchAction, ssePublisher);

        assertThat(nextFetchAction).isNull();

        verify(videoRepository).saveAll(anyList());
        verify(fetchActionService).actionSuccessful(fetchAction, 1);
        verify(ssePublisher).publish(fetchDataEvent.capture());

        assertThat(fetchDataEvent.getValue())
                .hasFieldOrPropertyWithValue("objectId", CHANNEL_ID);
        assertThat(fetchDataEvent.getValue().items())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("id", VIDEO_ID);
    }

    private static Activity buildUploadActivity() {
        var upload = new ActivityContentDetailsUpload();
        upload.setVideoId(VIDEO_ID);

        var contentDetails = new ActivityContentDetails();
        contentDetails.setUpload(upload);

        var snippet = new ActivitySnippet();
        snippet.setPublishedAt(new DateTime("2025-01-01T00:00:00Z"));

        var activity = new Activity();
        activity.setContentDetails(contentDetails);
        activity.setSnippet(snippet);
        return activity;
    }

    private static Activity buildPlaylistItemActivity() {
        var playlistItem = new ActivityContentDetailsPlaylistItem();
        playlistItem.setPlaylistId(PLAYLIST_ID);

        var contentDetails = new ActivityContentDetails();
        contentDetails.setPlaylistItem(playlistItem);

        var snippet = new ActivitySnippet();
        snippet.setPublishedAt(new DateTime("2025-01-01T00:00:00Z"));

        var activity = new Activity();
        activity.setContentDetails(contentDetails);
        activity.setSnippet(snippet);
        return activity;
    }
}

package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.repository.VideoRepository;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityContentDetails;
import com.google.api.services.youtube.model.ActivityContentDetailsPlaylistItem;
import com.google.api.services.youtube.model.ActivityContentDetailsUpload;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ActivitySnippet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private VideoRepository videoRepository;
    @Mock
    private YouTubeClientAdapter youTubeClientAdapter;

    @Test
    void fetch() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.VIDEOS)
                .objectId(CHANNEL_ID)
                .build();
        var activityListResponse = new ActivityListResponse();
        activityListResponse.setItems(List.of(buildUploadActivity(), buildPlaylistItemActivity()));

        when(youTubeClientAdapter.getActivities(fetchAction)).thenReturn(activityListResponse);

        var fetchResult = fetchVideosActionHandler.fetch(fetchAction);

        assertThat(fetchResult)
                .hasFieldOrPropertyWithValue("actionType", FetchAction.ActionType.VIDEOS)
                .hasFieldOrPropertyWithValue("objectId", CHANNEL_ID)
                .hasFieldOrPropertyWithValue("nextFetchAction", null);
        assertThat(fetchResult.items())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("id", VIDEO_ID);

        verify(videoRepository).saveAll(anyList());
    }

    private Activity buildUploadActivity() {
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

    private Activity buildPlaylistItemActivity() {
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

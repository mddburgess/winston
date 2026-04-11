package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.service.fetch.FetchResult;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FetchVideosActionTest {

    private static final WinstonFaker faker = new WinstonFaker();

    @InjectMocks
    private FetchVideosAction fetchVideosAction;

    @Mock
    private FetchVideosFromActivitiesAction fetchVideosFromActivitiesAction;
    @Mock
    private FetchVideosFromPlaylistItemsAction fetchVideosFromPlaylistItemsAction;

    @Mock
    private FetchResult<Video> fetchResult;

    @Test
    void fetch_recentVideos() {
        var fetchAction = FetchActionEntity.builder()
                .publishedAfter(faker.timeAndDate().past().atOffset(ZoneOffset.UTC))
                .build();

        when(fetchVideosFromActivitiesAction.fetch(fetchAction))
                .thenReturn(fetchResult);

        var result = fetchVideosAction.fetch(fetchAction);

        assertThat(result)
                .isEqualTo(fetchResult);
    }

    @Test
    void fetch_allVideos() {
        var fetchAction = FetchActionEntity.builder()
                .publishedAfter(null)
                .build();

        when(fetchVideosFromPlaylistItemsAction.fetch(fetchAction))
                .thenReturn(fetchResult);

        var result = fetchVideosAction.fetch(fetchAction);

        assertThat(result)
                .isEqualTo(fetchResult);
    }
}

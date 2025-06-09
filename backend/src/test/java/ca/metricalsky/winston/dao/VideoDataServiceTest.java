package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.entity.view.VideoCountView;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoDataServiceTest {

    @InjectMocks
    private VideoDataService videoDataService;

    @Mock
    private VideoRepository videoRepository;

    @Test
    void countAllByChannelId() {
        var videoCountView = mockVideoCountView();

        when(videoRepository.countAllByChannelId())
                .thenReturn(List.of(videoCountView));

        var videoCounts = videoDataService.countAllVideosByChannelId();

        assertThat(videoCounts)
                .hasSize(1)
                .containsEntry(videoCountView.getChannelId(), videoCountView.getVideos());

    }

    @Test
    void countAllByChannelId_defaultValue() {
        when(videoRepository.countAllByChannelId())
                .thenReturn(List.of());

        var videoCounts = videoDataService.countAllVideosByChannelId();

        assertThat(videoCounts.get("defaultValue"))
                .isEqualTo(0);
    }

    private static VideoCountView mockVideoCountView() {
        var videoCountView = mock(VideoCountView.class);
        lenient().when(videoCountView.getChannelId())
                .thenReturn(TestUtils.randomId());
        lenient().when(videoCountView.getVideos())
                .thenReturn(1);
        return videoCountView;
    }
}

package ca.metricalsky.winston.service.pull;

import ca.metricalsky.winston.entity.view.CommentStatisticsView;
import ca.metricalsky.winston.repository.VideoCommentsRepository;
import ca.metricalsky.winston.test.annotations.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@UnitTest
class PullStatisticsServiceTest {

    @InjectMocks
    private PullStatisticsService pullStatisticsService;

    @Mock
    private VideoCommentsRepository videoCommentsRepository;
    @Mock
    private CommentStatisticsView commentStatistics;

    @BeforeEach
    void beforeEach() {
        when(videoCommentsRepository.getCommentStatistics())
                .thenReturn(commentStatistics);
    }

    @Test
    void getTopLevelCommentPercentage() {
        when(commentStatistics.getCommentCount())
                .thenReturn(1L);
        when(commentStatistics.getReplyCount())
                .thenReturn(3L);

        var result = pullStatisticsService.getTopLevelCommentPercentage();

        assertThat(result)
                .isEqualTo(0.25);
    }

    @Test
    void getTopLevelCommentPercentage_zeroComments() {
        when(commentStatistics.getCommentCount())
                .thenReturn(0L);
        when(commentStatistics.getReplyCount())
                .thenReturn(0L);

        var result = pullStatisticsService.getTopLevelCommentPercentage();

        assertThat(result)
                .isEqualTo(0);
    }

    @Test
    void getAverageCommentsPerVideo() {
        when(commentStatistics.getCommentCount())
                .thenReturn(10L);
        when(commentStatistics.getVideoCount())
                .thenReturn(4L);

        var result = pullStatisticsService.getAverageCommentsPerVideo();

        assertThat(result)
                .isEqualTo(2.5);
    }

    @Test
    void getAverageCommentsPerVideo_zeroVideos() {
        when(commentStatistics.getCommentCount())
                .thenReturn(0L);
        when(commentStatistics.getVideoCount())
                .thenReturn(0L);

        var result = pullStatisticsService.getAverageCommentsPerVideo();

        assertThat(result)
                .isEqualTo(0);
    }
}

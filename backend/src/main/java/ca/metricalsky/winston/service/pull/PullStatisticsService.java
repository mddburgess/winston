package ca.metricalsky.winston.service.pull;

import ca.metricalsky.winston.database.repository.video.VideoCommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PullStatisticsService {

    private final VideoCommentsRepository videoCommentsRepository;

    public double getTopLevelCommentPercentage() {
        var commentStatistics = videoCommentsRepository.getCommentStatistics();
        var commentCount = commentStatistics.getCommentCount();
        var totalCommentCount = commentCount + commentStatistics.getReplyCount();

        if (totalCommentCount == 0) {
            return 0;
        }
        return (double) commentCount / totalCommentCount;
    }

    public double getAverageCommentsPerVideo() {
        var commentStatistics = videoCommentsRepository.getCommentStatistics();
        var commentCount = commentStatistics.getCommentCount();
        var videoCount = commentStatistics.getVideoCount();

        if (videoCount == 0) {
            return 0;
        }
        return (double) commentCount / videoCount;
    }
}

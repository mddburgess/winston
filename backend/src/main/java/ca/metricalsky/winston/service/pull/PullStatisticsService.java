package ca.metricalsky.winston.service.pull;

import ca.metricalsky.winston.repository.VideoCommentsRepository;
import ca.metricalsky.winston.repository.fetch.YouTubeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PullStatisticsService {

    private final VideoCommentsRepository videoCommentsRepository;

    public double getTopLevelCommentPercentage() {
        var commentStatistics = videoCommentsRepository.getCommentStatistics();
        var commentCount = commentStatistics.getCommentCount();
        var replyCount = commentStatistics.getReplyCount();

        return (double) commentCount / (commentCount + replyCount);
    }

    public double getAverageCommentsPerVideo() {
        var commentStatistics = videoCommentsRepository.getCommentStatistics();
        var commentCount = commentStatistics.getCommentCount();
        var videoCount = commentStatistics.getVideoCount();

        return (double) commentCount / videoCount;
    }
}

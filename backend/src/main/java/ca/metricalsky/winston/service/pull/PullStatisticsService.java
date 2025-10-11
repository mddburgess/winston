package ca.metricalsky.winston.service.pull;

import ca.metricalsky.winston.repository.VideoCommentsRepository;
import ca.metricalsky.winston.repository.fetch.YouTubeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PullStatisticsService {

    private final VideoCommentsRepository videoCommentsRepository;
    private final YouTubeRequestRepository youTubeRequestRepository;

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

    public double getAverageRepliesPerVideo() {
        var commentStatistics = videoCommentsRepository.getCommentStatistics();
        var replyCount = commentStatistics.getReplyCount();
        var videoCount = commentStatistics.getVideoCount();

        return (double) replyCount / videoCount;
    }

    public double getAverageCommentsPerPullRequest() {
        return youTubeRequestRepository.getAverageItemCountForCommentRequests();
    }

}

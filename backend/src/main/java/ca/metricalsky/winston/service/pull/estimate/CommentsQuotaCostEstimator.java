package ca.metricalsky.winston.service.pull.estimate;

import ca.metricalsky.winston.api.model.PullCommentsOperation;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.api.model.VideoDetails;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.service.pull.PullStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentsQuotaCostEstimator implements QuotaCostEstimator<PullCommentsOperation> {

    private final PullStatisticsService pullStatisticsService;
    private final VideoDataService videoDataService;

    @Override
    public int estimateQuotaCost(PullCommentsOperation operation) {
        var commentCount = videoDataService.findVideoById(operation.getVideoId())
                .map(Video::getDetails)
                .map(VideoDetails::getCommentCount)
                .map(this::estimateTopLevelCommentCount)
                .orElseGet(pullStatisticsService::getAverageCommentsPerVideo);

        var estimate = Math.ceil(commentCount / pullStatisticsService.getAverageCommentsPerPullRequest());

        operation.setEstimatedCost(Math.max((int) estimate, 1));
        return operation.getEstimatedCost();
    }

    private double estimateTopLevelCommentCount(int totalCommentCount) {
        var topLevelCommentPercentage = pullStatisticsService.getTopLevelCommentPercentage();
        return totalCommentCount * topLevelCommentPercentage;
    }
}

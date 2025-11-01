package ca.metricalsky.winston.service.pull.estimate;

import ca.metricalsky.winston.api.model.PullRepliesOperation;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.api.model.VideoDetails;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.repository.CommentRepository;
import ca.metricalsky.winston.service.pull.PullStatisticsService;
import ca.metricalsky.winston.utils.NumberUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepliesQuotaCostEstimator implements QuotaCostEstimator<PullRepliesOperation> {

    private final CommentRepository commentRepository;
    private final PullStatisticsService pullStatisticsService;
    private final VideoDataService videoDataService;

    @Override
    public int estimateQuotaCost(PullRepliesOperation operation) {
        if (operation.getCommentId() != null) {
            estimateQuotaCostForComment(operation);
        } else {
            estimateQuotaCostForVideo(operation);
        }
        return operation.getEstimatedCost();
    }

    private void estimateQuotaCostForVideo(PullRepliesOperation operation) {
        var commentCount = videoDataService.findVideoById(operation.getVideoId())
                .map(Video::getDetails)
                .map(VideoDetails::getCommentCount)
                .map(this::estimateTopLevelCommentCount)
                .orElseGet(pullStatisticsService::getAverageCommentsPerVideo);

        var estimatedCost = Math.max((int) Math.ceil(commentCount / 100), 1);
        operation.setEstimatedCost(estimatedCost);
    }

    private double estimateTopLevelCommentCount(int totalCommentCount) {
        var topLevelCommentPercentage = pullStatisticsService.getTopLevelCommentPercentage();
        return totalCommentCount * topLevelCommentPercentage;
    }

    private void estimateQuotaCostForComment(PullRepliesOperation operation) {
        var estimatedCost = 1;

        var maybeStatistics = commentRepository.getReplyStatisticsByCommentId(operation.getCommentId());
        if (maybeStatistics.isPresent()) {
            var statistics = maybeStatistics.get();

            var value = statistics.getFetchedReplyCount();
            var asOfDate = statistics.getMostRecentReplyPublishedAt();

            if (value == null || asOfDate == null || asOfDate.isBefore(statistics.getCommentLastFetchedAt())) {
                value = statistics.getCommentReplyCount();
                asOfDate  = statistics.getCommentLastFetchedAt();
            }

            estimatedCost = Math.max(NumberUtils.scaleToNow(value, statistics.getCommentPublishedAt(), asOfDate), 1);
        }


        operation.setEstimatedCost(estimatedCost);
    }
}

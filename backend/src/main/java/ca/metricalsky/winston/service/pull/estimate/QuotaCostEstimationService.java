package ca.metricalsky.winston.service.pull.estimate;

import ca.metricalsky.winston.api.model.PullChannelOperation;
import ca.metricalsky.winston.api.model.PullCommentsOperation;
import ca.metricalsky.winston.api.model.PullOperation;
import ca.metricalsky.winston.api.model.PullRepliesOperation;
import ca.metricalsky.winston.api.model.PullVideosOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuotaCostEstimationService {

    private final ChannelQuotaCostEstimator channelQuotaCostEstimator;
    private final CommentsQuotaCostEstimator commentsQuotaCostEstimator;
    private final RepliesQuotaCostEstimator repliesQuotaCostEstimator;
    private final VideosQuotaCostEstimator videosQuotaCostEstimator;

    public int estimateQuotaCost(List<PullOperation> pullOperations) {
        return pullOperations.stream()
                .mapToInt(this::estimateQuotaCost)
                .sum();
    }

    private int estimateQuotaCost(PullOperation operation) {
        return switch (operation) {
            case PullChannelOperation channel -> channelQuotaCostEstimator.estimateQuotaCost(channel);
            case PullVideosOperation videos -> videosQuotaCostEstimator.estimateQuotaCost(videos);
            case PullCommentsOperation comments -> commentsQuotaCostEstimator.estimateQuotaCost(comments);
            case PullRepliesOperation replies -> repliesQuotaCostEstimator.estimateQuotaCost(replies);
            case null, default -> throw new IllegalArgumentException();
        };
    }
}

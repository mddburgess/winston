package ca.metricalsky.winston.service.pull.estimate;

import ca.metricalsky.winston.api.model.PullVideosOperation;
import ca.metricalsky.winston.api.model.PullVideosOperation.RangeEnum;
import ca.metricalsky.winston.entity.view.ChannelVideoStatisticsView;
import ca.metricalsky.winston.repository.ChannelRepository;
import ca.metricalsky.winston.utils.NumberUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@Service
@RequiredArgsConstructor
public class VideosQuotaCostEstimator implements QuotaCostEstimator<PullVideosOperation> {

    private final ChannelRepository channelRepository;

    @Override
    public int estimateQuotaCost(PullVideosOperation operation) {
        var channelHandle = operation.getChannelHandle();
        var statistics = channelRepository.findChannelVideoStatisticsByCustomUrl(channelHandle);

        var channelDataAvailable = statistics.map(this::isChannelDataAvailable).orElse(false);
        var estimatedVideoCount = channelDataAvailable
                ? estimateVideoCountForChannel(statistics.get(), operation.getRange())
                : estimateAverageVideoCount();

        var estimatedCountPerRequest = operation.getRange() == RangeEnum.ALL ? 50 : 25;

        return estimateCost(estimatedVideoCount, estimatedCountPerRequest);
    }

    private boolean isChannelDataAvailable(ChannelVideoStatisticsView statistics) {
        return statistics.getChannelVideoCount() != null || statistics.getVideoCount() > 0;
    }

    private int estimateVideoCountForChannel(ChannelVideoStatisticsView statistics, RangeEnum operationRange) {
        var channelVideoCount = firstNonNull(statistics.getChannelVideoCount(), 0);
        var pulledVideoCount = getVideoCountScaledToNow(statistics);
        var estimatedVideoCount = Math.max(channelVideoCount, pulledVideoCount);

        if (operationRange == RangeEnum.LATEST) {
            estimatedVideoCount -= statistics.getVideoCount();
        }

        return estimatedVideoCount;
    }

    private int getVideoCountScaledToNow(ChannelVideoStatisticsView statistics) {
        return NumberUtils.scaleToNow(statistics.getVideoCount(),
                statistics.getChannelPublishedAt(), statistics.getLatestVideoPublishedAt());
    }

    private int estimateAverageVideoCount() {
        return 0;
    }

    private int estimateCost(int estimatedVideoCount, int estimatedCountPerRequest) {
        var estimatedRequestCount = Math.ceilDiv(estimatedVideoCount, estimatedCountPerRequest) * 2;
        return Math.max(estimatedRequestCount, 2);
    }
}

package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchVideosOperation extends BasicFetchOperation<Video> {

    private final ChannelDataService channelDataService;
    private final VideoRepository videoRepository;

    @Autowired
    public FetchVideosOperation(
            FetchActionHandler<Video> fetchActionHandler,
            ChannelDataService channelDataService,
            VideoRepository videoRepository
    ) {
        super(fetchActionHandler);
        this.channelDataService = channelDataService;
        this.videoRepository = videoRepository;
    }

    @Override
    FetchActionEntity getFirstFetchAction(FetchOperationEntity fetchOperation) {
        var channel = channelDataService.findChannelByHandle(fetchOperation.getObjectId())
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_PULLED));

        var publishedAfter = fetchOperation.getPublishedAfter();
        if (publishedAfter == null && "LATEST".equals(fetchOperation.getMode())) {
            publishedAfter = videoRepository.findLastPublishedAtForChannelId(channel.getId())
                    .map(date -> date.plusSeconds(1))
                    .orElse(null);
        }

        return FetchActionEntity.builder()
                .fetchOperationId(fetchOperation.getId())
                .actionType(FetchActionEntity.Type.valueOf(fetchOperation.getOperationType().name()))
                .objectId(fetchOperation.getObjectId())
                .publishedAfter(publishedAfter)
                .publishedBefore(fetchOperation.getPublishedBefore())
                .build();
    }
}

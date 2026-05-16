package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.database.repository.video.VideoRepository;
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

        var objectId = fetchOperation.getObjectId();
        if (publishedAfter == null) {
            objectId = channel.getUploadsPlaylistId();
        }

        var fetchAction = new FetchActionEntity();
        fetchAction.setFetchOperationId(fetchOperation.getId());
        fetchAction.setActionType(FetchActionEntity.Type.valueOf(fetchOperation.getOperationType().name()));
        fetchAction.setObjectId(objectId);
        fetchAction.setPublishedAfter(publishedAfter);
        fetchAction.setPublishedBefore(fetchOperation.getPublishedBefore());
        return fetchAction;
    }
}

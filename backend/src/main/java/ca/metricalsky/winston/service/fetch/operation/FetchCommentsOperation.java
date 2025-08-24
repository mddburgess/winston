package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchCommentsOperation extends BasicFetchOperation<TopLevelComment> {

    private final VideoCommentsService videoCommentsService;

    @Autowired
    public FetchCommentsOperation(
            FetchActionHandler<TopLevelComment> fetchCommentsActionHandler,
            VideoCommentsService videoCommentsService
    ) {
        super(fetchCommentsActionHandler);
        this.videoCommentsService = videoCommentsService;
    }

    @Override
    public void afterFetch(FetchOperationEntity fetchOperation) {
        videoCommentsService.updateVideoComments(fetchOperation.getObjectId());
    }
}

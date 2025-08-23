package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchCommentsOperationHandler extends DefaultFetchOperationHandler {

    private final FetchActionHandler<TopLevelComment> fetchCommentsActionHandler;
    private final VideoCommentsService videoCommentsService;

    @Autowired
    public FetchCommentsOperationHandler(
            FetchOperationService fetchOperationService,
            SsePublisher ssePublisher,
            FetchActionHandler<TopLevelComment> fetchCommentsActionHandler,
            VideoCommentsService videoCommentsService
    ) {
        super(fetchOperationService, ssePublisher);
        this.fetchCommentsActionHandler = fetchCommentsActionHandler;
        this.videoCommentsService = videoCommentsService;
    }

    @Override
    protected FetchActionHandler<TopLevelComment> getFetchActionHandler() {
        return fetchCommentsActionHandler;
    }

    @Override
    public void afterFetch(FetchOperationEntity fetchOperation) {
        videoCommentsService.updateVideoComments(fetchOperation.getObjectId());
    }
}

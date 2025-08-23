package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchCommentsActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchCommentsOperationHandler extends DefaultFetchOperationHandler {

    private final FetchCommentsActionHandler fetchCommentsActionHandler;
    private final VideoCommentsService videoCommentsService;

    @Autowired
    public FetchCommentsOperationHandler(
            FetchOperationService fetchOperationService,
            SsePublisher ssePublisher,
            FetchCommentsActionHandler fetchCommentsActionHandler,
            VideoCommentsService videoCommentsService
    ) {
        super(fetchOperationService, ssePublisher);
        this.fetchCommentsActionHandler = fetchCommentsActionHandler;
        this.videoCommentsService = videoCommentsService;
    }

    @Override
    protected FetchActionHandler<?> getFetchActionHandler() {
        return fetchCommentsActionHandler;
    }

    @Override
    public void afterFetch(FetchOperationEntity fetchOperation) {
        videoCommentsService.updateVideoComments(fetchOperation.getObjectId());
    }
}

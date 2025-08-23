package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchVideosOperationHandler extends DefaultFetchOperationHandler {

    private final FetchActionHandler<Video> fetchVideosActionHandler;

    @Autowired
    public FetchVideosOperationHandler(
            FetchOperationService fetchOperationService,
            SsePublisher ssePublisher,
            FetchActionHandler<Video> fetchVideosActionHandler
    ) {
        super(fetchOperationService, ssePublisher);
        this.fetchVideosActionHandler = fetchVideosActionHandler;
    }

    @Override
    protected FetchActionHandler<Video> getFetchActionHandler() {
        return fetchVideosActionHandler;
    }
}

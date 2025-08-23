package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchVideosActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchVideosOperationHandler extends DefaultFetchOperationHandler {

    private final FetchVideosActionHandler fetchVideosActionHandler;

    @Autowired
    public FetchVideosOperationHandler(
            FetchOperationService fetchOperationService,
            SsePublisher ssePublisher,
            FetchVideosActionHandler fetchVideosActionHandler
    ) {
        super(fetchOperationService, ssePublisher);
        this.fetchVideosActionHandler = fetchVideosActionHandler;
    }

    @Override
    protected FetchActionHandler<?> getFetchActionHandler() {
        return fetchVideosActionHandler;
    }
}

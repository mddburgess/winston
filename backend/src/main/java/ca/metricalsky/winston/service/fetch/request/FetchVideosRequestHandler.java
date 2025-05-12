package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchVideosActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchVideosRequestHandler extends DefaultFetchRequestHandler {

    private final FetchVideosActionHandler fetchVideosActionHandler;

    @Autowired
    public FetchVideosRequestHandler(
            FetchRequestService fetchRequestService,
            FetchVideosActionHandler fetchVideosActionHandler
    ) {
        super(fetchRequestService);
        this.fetchVideosActionHandler = fetchVideosActionHandler;
    }

    @Override
    protected FetchActionHandler<?> getFetchActionHandler() {
        return fetchVideosActionHandler;
    }
}

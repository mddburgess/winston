package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchRepliesActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchCommentRepliesRequestHandler extends DefaultFetchRequestHandler {

    private final FetchRepliesActionHandler fetchRepliesActionHandler;

    @Autowired
    public FetchCommentRepliesRequestHandler(
            FetchRequestService fetchRequestService,
            FetchRepliesActionHandler fetchRepliesActionHandler
    ) {
        super(fetchRequestService);
        this.fetchRepliesActionHandler = fetchRepliesActionHandler;
    }

    @Override
    protected FetchActionHandler<?> getFetchActionHandler() {
        return fetchRepliesActionHandler;
    }
}

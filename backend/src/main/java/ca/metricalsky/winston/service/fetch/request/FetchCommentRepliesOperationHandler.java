package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchRepliesActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchCommentRepliesOperationHandler extends DefaultFetchOperationHandler {

    private final FetchRepliesActionHandler fetchRepliesActionHandler;

    @Autowired
    public FetchCommentRepliesOperationHandler(
            FetchOperationService fetchOperationService,
            FetchRepliesActionHandler fetchRepliesActionHandler
    ) {
        super(fetchOperationService);
        this.fetchRepliesActionHandler = fetchRepliesActionHandler;
    }

    @Override
    protected FetchActionHandler<?> getFetchActionHandler() {
        return fetchRepliesActionHandler;
    }
}

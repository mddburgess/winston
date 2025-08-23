package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchCommentRepliesOperationHandler extends DefaultFetchOperationHandler {

    private final FetchActionHandler<Comment> fetchRepliesActionHandler;

    @Autowired
    public FetchCommentRepliesOperationHandler(
            FetchOperationService fetchOperationService,
            SsePublisher ssePublisher,
            FetchActionHandler<Comment> fetchRepliesActionHandler
    ) {
        super(fetchOperationService, ssePublisher);
        this.fetchRepliesActionHandler = fetchRepliesActionHandler;
    }

    @Override
    protected FetchActionHandler<Comment> getFetchActionHandler() {
        return fetchRepliesActionHandler;
    }
}

package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchCommentsActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchCommentsRequestHandler extends DefaultFetchRequestHandler {

    private final FetchCommentsActionHandler fetchCommentsActionHandler;

    @Autowired
    public FetchCommentsRequestHandler(
            FetchRequestService fetchRequestService,
            FetchCommentsActionHandler fetchCommentsActionHandler
    ) {
        super(fetchRequestService);
        this.fetchCommentsActionHandler = fetchCommentsActionHandler;
    }

    @Override
    protected FetchActionHandler<?> getFetchActionHandler() {
        return fetchCommentsActionHandler;
    }
}

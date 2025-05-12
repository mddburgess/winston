package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchCommentsActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchCommentsRequestHandler extends DefaultFetchRequestHandler {

    private final FetchCommentsActionHandler fetchCommentsActionHandler;
    private final VideoCommentsService videoCommentsService;

    @Autowired
    public FetchCommentsRequestHandler(
            FetchRequestService fetchRequestService,
            FetchCommentsActionHandler fetchCommentsActionHandler,
            VideoCommentsService videoCommentsService
    ) {
        super(fetchRequestService);
        this.fetchCommentsActionHandler = fetchCommentsActionHandler;
        this.videoCommentsService = videoCommentsService;
    }

    @Override
    protected FetchActionHandler<?> getFetchActionHandler() {
        return fetchCommentsActionHandler;
    }

    @Override
    public void afterFetch(FetchRequest fetchRequest) {
        videoCommentsService.updateVideoComments(fetchRequest.getObjectId());
    }
}

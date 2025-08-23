package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchChannelsOperationHandler extends DefaultFetchOperationHandler {

    private final FetchActionHandler<Channel> fetchChannelActionHandler;

    @Autowired
    public FetchChannelsOperationHandler(
            FetchOperationService fetchOperationService,
            SsePublisher ssePublisher,
            FetchActionHandler<Channel> fetchChannelActionHandler
    ) {
        super(fetchOperationService, ssePublisher);
        this.fetchChannelActionHandler = fetchChannelActionHandler;
    }

    @Override
    protected FetchActionHandler<Channel> getFetchActionHandler() {
        return fetchChannelActionHandler;
    }
}

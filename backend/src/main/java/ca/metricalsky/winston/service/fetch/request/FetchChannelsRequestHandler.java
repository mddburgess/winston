package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchChannelActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchChannelsRequestHandler extends DefaultFetchRequestHandler {

    private final FetchChannelActionHandler fetchChannelActionHandler;

    @Autowired
    public FetchChannelsRequestHandler(
            FetchRequestService fetchRequestService,
            FetchChannelActionHandler fetchChannelActionHandler
    ) {
        super(fetchRequestService);
        this.fetchChannelActionHandler = fetchChannelActionHandler;
    }

    @Override
    protected FetchActionHandler<?> getFetchActionHandler() {
        return fetchChannelActionHandler;
    }
}

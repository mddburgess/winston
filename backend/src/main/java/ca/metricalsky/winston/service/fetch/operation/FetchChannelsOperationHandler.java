package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchChannelActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchChannelsOperationHandler extends DefaultFetchOperationHandler {

    private final FetchChannelActionHandler fetchChannelActionHandler;

    @Autowired
    public FetchChannelsOperationHandler(
            FetchOperationService fetchOperationService,
            FetchChannelActionHandler fetchChannelActionHandler
    ) {
        super(fetchOperationService);
        this.fetchChannelActionHandler = fetchChannelActionHandler;
    }

    @Override
    protected FetchActionHandler<?> getFetchActionHandler() {
        return fetchChannelActionHandler;
    }
}

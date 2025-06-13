package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FetchChannelActionHandler extends FetchActionHandler<Channel> {

    private final ChannelDataService channelDataService;
    private final YouTubeService youTubeService;

    public FetchChannelActionHandler(
            FetchActionService fetchActionService,
            ChannelDataService channelDataService,
            YouTubeService youTubeService
    ) {
        super(fetchActionService);
        this.channelDataService = channelDataService;
        this.youTubeService = youTubeService;
    }

    @Override
    protected FetchResult<Channel> doFetch(FetchActionEntity fetchAction) {
        var channelListResponse = youTubeService.getChannels(fetchAction);
        var channel = channelDataService.saveChannel(channelListResponse)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "The requested channel does not exist."));
        return new FetchResult<>(fetchAction, channel, null);
    }
}

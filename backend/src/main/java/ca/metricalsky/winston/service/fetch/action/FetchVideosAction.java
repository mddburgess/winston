package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.service.fetch.FetchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchVideosAction implements FetchAction<Video> {

    private final FetchVideosFromActivitiesAction fetchVideosFromActivitiesAction;
    private final FetchVideosFromPlaylistItemsAction fetchVideosFromPlaylistItemsAction;

    @Override
    public FetchResult<Video> fetch(FetchActionEntity fetchAction) {
        if (fetchAction.getPublishedAfter() != null) {
            return fetchVideosFromActivitiesAction.fetch(fetchAction);
        }
        return fetchVideosFromPlaylistItemsAction.fetch(fetchAction);
    }
}

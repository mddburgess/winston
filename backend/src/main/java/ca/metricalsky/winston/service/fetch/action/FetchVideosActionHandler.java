package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.mapper.entity.OffsetDateTimeMapper;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ActivitySnippet;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class FetchVideosActionHandler extends FetchActionHandler<Video> {

    private final OffsetDateTimeMapper offsetDateTimeMapper = new OffsetDateTimeMapper();

    private final VideoDataService videoDataService;
    private final YouTubeService youTubeService;

    public FetchVideosActionHandler(
            FetchActionService fetchActionService,
            VideoDataService videoDataService,
            YouTubeService youTubeService
    ) {
        super(fetchActionService);
        this.videoDataService = videoDataService;
        this.youTubeService = youTubeService;
    }

    @Override
    protected FetchResult<Video> doFetch(FetchActionEntity fetchAction) {
        var activityListResponse = youTubeService.getActivities(fetchAction);
        var videos = videoDataService.saveVideos(activityListResponse);
        var nextFetchAction = getNextFetchAction(fetchAction, activityListResponse);

        return new FetchResult<>(fetchAction, videos, nextFetchAction);
    }

    private FetchActionEntity getNextFetchAction(
            FetchActionEntity fetchAction,
            ActivityListResponse activityListResponse
    ) {
        if (activityListResponse.getNextPageToken() == null) {
            return null;
        }

        var activities = activityListResponse.getItems()
                .stream()
                .filter(activity -> activity.getContentDetails().getUpload() != null)
                .toList();
        if (activities.isEmpty()) {
            activities = activityListResponse.getItems();
        }
        var nextPublishedBefore = activities.stream()
                .map(Activity::getSnippet)
                .map(ActivitySnippet::getPublishedAt)
                .map(offsetDateTimeMapper::fromYouTube)
                .min(Comparator.naturalOrder())
                .map(publishedAt -> publishedAt.minusSeconds(1))
                .orElse(null);

        return FetchActionEntity.builder()
                .fetchOperationId(fetchAction.getFetchOperationId())
                .actionType(fetchAction.getActionType())
                .objectId(fetchAction.getObjectId())
                .publishedAfter(fetchAction.getPublishedAfter())
                .publishedBefore(nextPublishedBefore)
                .build();
    }
}

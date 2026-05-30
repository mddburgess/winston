package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityContentDetails;
import com.google.api.services.youtube.model.ActivityContentDetailsUpload;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ActivitySnippet;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FetchVideosFromActivitiesAction implements FetchAction<Video> {


    private final ChannelDataService channelDataService;
    private final ConversionService conversionService;
    private final VideoDataService videoDataService;
    private final YouTubeService youTubeService;

    @Override
    public FetchResult<Video> fetch(FetchActionEntity fetchAction) {
        if (fetchAction.getObjectId().startsWith("@")) {
            var channel = channelDataService.findChannelByHandle(fetchAction.getObjectId())
                    .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_PULLED));
            fetchAction.setObjectId(channel.getId());
        }
        var activityListResponse = youTubeService.getActivities(fetchAction);
        var nextFetchAction = getNextFetchAction(fetchAction, activityListResponse);

        var videoIds = Optional.ofNullable(activityListResponse.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(Activity::getContentDetails)
                .map(ActivityContentDetails::getUpload)
                .filter(Objects::nonNull)
                .map(ActivityContentDetailsUpload::getVideoId)
                .toList();
        if (videoIds.isEmpty()) {
            return new FetchResult<>(fetchAction, Collections.emptyList(), nextFetchAction);
        }

        var videosResponse = youTubeService.getVideos(fetchAction.getId(), videoIds);
        var videos = videoDataService.saveVideos(videosResponse);

        return new FetchResult<>(fetchAction, videos, nextFetchAction);
    }

    private FetchActionEntity getNextFetchAction(
            FetchActionEntity fetchAction,
            ActivityListResponse activityListResponse
    ) {
        var nextPageToken = activityListResponse.getNextPageToken();
        if (nextPageToken == null || nextPageToken.equals(fetchAction.getPageToken())) {
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
                .map(publishedAt -> conversionService.convert(publishedAt, OffsetDateTime.class))
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .map(publishedAt -> publishedAt.minusSeconds(1))
                .orElse(null);

        var nextFetchAction = new FetchActionEntity();
        nextFetchAction.setFetchOperationId(fetchAction.getFetchOperationId());
        nextFetchAction.setActionType(fetchAction.getActionType());
        nextFetchAction.setObjectId(fetchAction.getObjectId());
        nextFetchAction.setPublishedAfter(fetchAction.getPublishedAfter());
        nextFetchAction.setPublishedBefore(nextPublishedBefore);
        return nextFetchAction;
    }
}

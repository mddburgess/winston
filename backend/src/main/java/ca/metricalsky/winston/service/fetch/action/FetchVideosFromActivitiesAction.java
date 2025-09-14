package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.mapper.entity.OffsetDateTimeMapper;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityContentDetails;
import com.google.api.services.youtube.model.ActivityContentDetailsUpload;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ActivitySnippet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FetchVideosFromActivitiesAction implements FetchAction<Video> {

    private final OffsetDateTimeMapper offsetDateTimeMapper = new OffsetDateTimeMapper();

    private final ChannelDataService channelDataService;
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

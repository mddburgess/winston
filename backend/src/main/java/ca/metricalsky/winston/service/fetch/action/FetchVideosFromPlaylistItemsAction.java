package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemContentDetails;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FetchVideosFromPlaylistItemsAction implements FetchAction<Video> {

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

        var playlistItemsResponse = youTubeService.getPlaylistItems(fetchAction);
        var nextFetchAction = getNextFetchAction(fetchAction, playlistItemsResponse);

        var videoIds = Optional.ofNullable(playlistItemsResponse.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(PlaylistItem::getContentDetails)
                .map(PlaylistItemContentDetails::getVideoId)
                .toList();
        if (videoIds.isEmpty()) {
            return new FetchResult<>(fetchAction, Collections.emptyList(), nextFetchAction);
        }

        var videosResponse = youTubeService.getVideos(fetchAction.getId(), videoIds);
        var videos = videoDataService.saveVideos(videosResponse);

        return new FetchResult<>(fetchAction, videos, nextFetchAction);
    }

    private static FetchActionEntity getNextFetchAction(
            FetchActionEntity fetchAction,
            PlaylistItemListResponse youTubeResponse
    ) {
        return youTubeResponse.getNextPageToken() == null ? null : FetchActionEntity.builder()
                .fetchOperationId(fetchAction.getFetchOperationId())
                .actionType(fetchAction.getActionType())
                .objectId(fetchAction.getObjectId())
                .pageToken(youTubeResponse.getNextPageToken())
                .build();
    }
}

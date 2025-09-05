package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        var videos = videoDataService.saveVideos(playlistItemsResponse);
        var nextFetchAction = getNextFetchAction(fetchAction, playlistItemsResponse);

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

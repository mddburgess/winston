package ca.metricalsky.winston.client;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class YouTubeClient {

    static final List<String> CHANNEL_PARTS = List.of(
            "brandingSettings", "contentDetails", "contentOwnerDetails", "id",
            "localizations", "snippet", "statistics", "status", "topicDetails"
    );

    static final List<String> ACTIVITY_PARTS = List.of(
            "contentDetails", "id", "snippet"
    );

    static final List<String> PLAYLIST_ITEM_PARTS = List.of(
            "contentDetails", "id", "snippet", "status"
    );

    static final List<String> VIDEO_PARTS = List.of(
            "contentDetails", "id", "liveStreamingDetails", "localizations", "paidProductPlacementDetails",
            "recordingDetails", "snippet", "statistics", "status", "topicDetails"
    );

    static final List<String> COMMENT_THREAD_PARTS = List.of(
            "id", "replies", "snippet"
    );

    static final List<String> COMMENT_PARTS = List.of(
            "id", "snippet"
    );

    private final YouTube youTube;

    public ChannelListResponse getChannel(String handle) {
        try {
            log.info("Fetching channels for handle '{}'", handle);

            var response = youTube.channels()
                    .list(CHANNEL_PARTS)
                    .setForHandle(handle)
                    .setMaxResults(50L)
                    .execute();

            var channelCount = firstNonNull(response.getItems(), List.of()).size();
            log.info("Fetched {} channels for handle '{}'", channelCount, handle);
            return response;
        } catch (IOException | RuntimeException ex) {
            log.error("Failed to fetch channels for handle '{}'", handle, ex);
            throw YouTubeException.wrap(ex);
        }
    }

    public ActivityListResponse getActivities(String channelId, String publishedAfter, String publishedBefore) {
        try {
            log.info("Fetching activities for channelId '{}' publishedAfter '{}' publishedBefore '{}'",
                    channelId, publishedAfter, publishedBefore);

            var response = youTube.activities()
                    .list(ACTIVITY_PARTS)
                    .setChannelId(channelId)
                    .setPublishedAfter(publishedAfter)
                    .setPublishedBefore(publishedBefore)
                    .setMaxResults(50L)
                    .execute();

            var activityCount = firstNonNull(response.getItems(), List.of()).size();
            log.info("Fetched {} activities for channelId '{}' publishedAfter '{}' publishedBefore '{}'",
                    activityCount, channelId, publishedAfter, publishedBefore);
            return response;
        } catch (IOException | RuntimeException ex) {
            log.error("Failed to fetch activities for channelId '{}' publishedAfter '{}' publishedBefore '{}'",
                    channelId, publishedAfter, publishedBefore, ex);
            throw YouTubeException.wrap(ex);
        }
    }

    public PlaylistItemListResponse getPlaylistItems(String playlistId, String pageToken) {
        try {
            log.info("Fetching playlist items for playlistId '{}' pageToken '{}'", playlistId, pageToken);

            var response = youTube.playlistItems()
                    .list(PLAYLIST_ITEM_PARTS)
                    .setPlaylistId(playlistId)
                    .setPageToken(pageToken)
                    .setMaxResults(50L)
                    .execute();

            var playlistItemCount = firstNonNull(response.getItems(), List.of()).size();
            log.info("Fetched {} playlist items for playlistId '{}' pageToken '{}'",
                    playlistItemCount, playlistId, pageToken);
            return response;
        } catch (IOException | RuntimeException ex) {
            log.error("Failed to fetch playlist items for playlistId '{}' pageToken '{}'", playlistId, pageToken, ex);
            throw YouTubeException.wrap(ex);
        }
    }

    public VideoListResponse getVideos(List<String> videoIds) {
        try {
            log.info("Fetching videos for videoIds {}", videoIds);

            var response = youTube.videos()
                    .list(VIDEO_PARTS)
                    .setId(videoIds)
                    .setMaxResults(50L)
                    .execute();

            var videoItemCount = firstNonNull(response.getItems(), List.of()).size();
            log.info("Fetched {} videos for videoIds {}", videoItemCount, videoIds);
            return response;
        } catch (IOException | RuntimeException ex) {
            log.error("Failed to fetch videos for videoIds {}", videoIds, ex);
            throw YouTubeException.wrap(ex);
        }
    }

    public CommentThreadListResponse getComments(String videoId, String pageToken) {
        try {
            log.info("Fetching comments for videoId '{}' pageToken '{}'", videoId, pageToken);

            var response = youTube.commentThreads()
                    .list(COMMENT_THREAD_PARTS)
                    .setVideoId(videoId)
                    .setPageToken(pageToken)
                    .setMaxResults(100L)
                    .execute();

            var commentCount = firstNonNull(response.getItems(), List.of()).size();
            log.info("Fetched {} comments for videoId '{}' pageToken '{}'", commentCount, videoId, pageToken);
            return response;
        } catch (IOException | RuntimeException ex) {
            log.error("Failed to fetch comments for videoId '{}' pageToken '{}'", videoId, pageToken, ex);
            throw YouTubeException.wrap(ex);
        }
    }

    public CommentListResponse getReplies(String commentId, String pageToken) {
        try {
            log.info("Fetching replies for commentId '{}' pageToken '{}'", commentId, pageToken);

            var response = youTube.comments()
                    .list(COMMENT_PARTS)
                    .setParentId(commentId)
                    .setMaxResults(100L)
                    .setPageToken(pageToken)
                    .execute();

            var replyCount = firstNonNull(response.getItems(), List.of()).size();
            log.info("Fetched {} replies for commentId '{}' pageToken '{}'", replyCount, commentId, pageToken);
            return response;
        } catch (IOException | RuntimeException ex) {
            log.error("Failed to fetch replies for commentId '{}' pageToken '{}'", commentId, pageToken, ex);
            throw YouTubeException.wrap(ex);
        }
    }
}

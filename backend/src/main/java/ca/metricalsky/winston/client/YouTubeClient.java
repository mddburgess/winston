package ca.metricalsky.winston.client;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class YouTubeClient {

    private static final List<String> CHANNEL_PARTS = List.of(
            "brandingSettings", "contentDetails", "contentOwnerDetails", "id",
            "localizations", "snippet", "statistics", "status", "topicDetails"
    );

    private static final List<String> ACTIVITY_PARTS = List.of(
            "contentDetails", "id", "snippet"
    );

    private static final List<String> COMMENT_THREAD_PARTS = List.of(
            "id", "replies", "snippet"
    );

    private static final List<String> COMMENT_PARTS = List.of(
            "id", "snippet"
    );

    private final YouTube youTube;

    public ChannelListResponse getChannel(String handle) throws IOException {
        try {
            log.info("Fetching channels for handle '{}'", handle);

            var response = youTube.channels()
                    .list(CHANNEL_PARTS)
                    .setForHandle(handle)
                    .setMaxResults(50L)
                    .execute();

            log.info("Fetched {} channels for handle '{}'", response.getItems().size(), handle);
            return response;
        } catch (IOException | RuntimeException ex) {
            log.error("Failed to fetch channels for handle '{}'", handle, ex);
            throw ex;
        }
    }

    public ActivityListResponse getActivities(String channelId, String publishedAfter, String publishedBefore)
            throws IOException {
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

            log.info("Fetched {} activities for channelId '{}' publishedAfter '{}' publishedBefore '{}'",
                    response.getItems().size(), channelId, publishedAfter, publishedBefore);
            return response;
        } catch (IOException | RuntimeException ex) {
            log.error("Failed to fetch activities for channelId '{}' publishedAfter '{}' publishedBefore '{}'",
                    channelId, publishedAfter, publishedBefore, ex);
            throw ex;
        }
    }

    public CommentThreadListResponse getComments(String videoId, String pageToken) throws IOException {
        try {
            log.info("Fetching comments for videoId '{}' pageToken '{}'", videoId, pageToken);

            var response = youTube.commentThreads()
                    .list(COMMENT_THREAD_PARTS)
                    .setVideoId(videoId)
                    .setPageToken(pageToken)
                    .setMaxResults(100L)
                    .execute();

            log.info("Fetched {} comments for videoId '{}' pageToken '{}'",
                    response.getItems().size(), videoId, pageToken);
            return response;
        } catch (IOException | RuntimeException ex) {
            log.error("Failed to fetch comments for videoId '{}' pageToken '{}'", videoId, pageToken, ex);
            throw ex;
        }
    }

    public CommentListResponse getReplies(String commentId, String pageToken) throws IOException {
        try {
            log.info("Fetching replies for commentId '{}' pageToken '{}'", commentId, pageToken);

            var response = youTube.comments()
                    .list(COMMENT_PARTS)
                    .setParentId(commentId)
                    .setMaxResults(100L)
                    .setPageToken(pageToken)
                    .execute();

            log.info("Fetched {} replies for commentId '{}' pageToken '{}'",
                    response.getItems().size(), commentId, pageToken);
            return response;
        } catch (IOException | RuntimeException ex) {
            log.error("Failed to fetch replies for commentId '{}' pageToken '{}'", commentId, pageToken, ex);
            throw ex;
        }
    }
}

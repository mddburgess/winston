package ca.metricalsky.yt.comments.client;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
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
        return youTube.channels()
                .list(CHANNEL_PARTS)
                .setForHandle(handle)
                .setMaxResults(50L)
                .execute();
    }

    public ActivityListResponse getActivities(String channelId, String pageToken) throws IOException {
        return youTube.activities()
                .list(ACTIVITY_PARTS)
                .setChannelId(channelId)
                .setPageToken(pageToken)
                .setMaxResults(50L)
                .execute();
    }

    public CommentThreadListResponse getComments(String videoId, String pageToken) throws IOException {
        return youTube.commentThreads()
                .list(COMMENT_THREAD_PARTS)
                .setVideoId(videoId)
                .setPageToken(pageToken)
                .setMaxResults(100L)
                .execute();
    }

    public CommentListResponse getReplies(String commentId, String pageToken) throws IOException {
        return youTube.comments()
                .list(COMMENT_PARTS)
                .setParentId(commentId)
                .setMaxResults(100L)
                .setPageToken(pageToken)
                .execute();
    }
}

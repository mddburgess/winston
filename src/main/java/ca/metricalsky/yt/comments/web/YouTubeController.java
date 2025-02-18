package ca.metricalsky.yt.comments.web;

import ca.metricalsky.yt.comments.client.YouTubeClient;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class YouTubeController {

    private final YouTubeClient youTubeClient;

    @Autowired
    public YouTubeController(YouTubeClient youTubeClient) {
        this.youTubeClient = youTubeClient;
    }

    @GetMapping("/channels/{handle}")
    public ChannelListResponse getChannelByHandle(@PathVariable String handle) throws IOException {
        return youTubeClient.getChannel(handle);
    }

    @GetMapping("/activities/{channelId}")
    public ActivityListResponse getActivitiesByChannelId(
            @PathVariable String channelId,
            @RequestParam(required = false) String pageToken
    ) throws IOException {
        return youTubeClient.getActivities(channelId, pageToken);
    }

    @GetMapping("/comments/{videoId}")
    public CommentThreadListResponse getCommentsByVideoId(
            @PathVariable String videoId,
            @RequestParam(required = false) String pageToken
    ) throws IOException {
        return youTubeClient.getComments(videoId, pageToken);
    }

    @GetMapping("/replies/{commentId}")
    public CommentListResponse getRepliesByCommentId(
            @PathVariable String commentId,
            @RequestParam(required = false) String pageToken
    ) throws IOException {
        return youTubeClient.getReplies(commentId, pageToken);
    }
}

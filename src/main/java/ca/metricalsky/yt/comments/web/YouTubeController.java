package ca.metricalsky.yt.comments.web;

import ca.metricalsky.yt.comments.client.YouTubeClient;
import ca.metricalsky.yt.comments.entity.Channel;
import ca.metricalsky.yt.comments.entity.Comment;
import ca.metricalsky.yt.comments.entity.Video;
import ca.metricalsky.yt.comments.mapper.ChannelMapper;
import ca.metricalsky.yt.comments.mapper.CommentMapper;
import ca.metricalsky.yt.comments.mapper.VideoMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class YouTubeController {

    private final ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);
    private final VideoMapper videoMapper = Mappers.getMapper(VideoMapper.class);
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    private final YouTubeClient youTubeClient;

    @Autowired
    public YouTubeController(YouTubeClient youTubeClient) {
        this.youTubeClient = youTubeClient;
    }

    @GetMapping("/channels/{handle}")
    public Channel getChannelByHandle(@PathVariable String handle) throws IOException {
        var channelListResponse = youTubeClient.getChannel(handle);
        return channelMapper.fromYouTube(channelListResponse.getItems().getFirst());
    }

    @GetMapping("/activities/{channelId}")
    public List<Video> getActivitiesByChannelId(
            @PathVariable String channelId,
            @RequestParam(required = false) String pageToken
    ) throws IOException {
        var activityListResponse = youTubeClient.getActivities(channelId, pageToken);
        return activityListResponse.getItems()
                .stream()
                .filter(activity -> activity.getContentDetails().getUpload() != null)
                .map(videoMapper::fromYouTube)
                .toList();
    }

    @GetMapping("/comments/{videoId}")
    public List<Comment> getCommentsByVideoId(
            @PathVariable String videoId,
            @RequestParam(required = false) String pageToken
    ) throws IOException {
        var commentThreadListResponse = youTubeClient.getComments(videoId, pageToken);
        return commentThreadListResponse.getItems()
                .stream()
                .map(commentMapper::fromYouTube)
                .toList();
    }

    @GetMapping("/replies/{commentId}")
    public List<Comment> getRepliesByCommentId(
            @PathVariable String commentId,
            @RequestParam(required = false) String pageToken
    ) throws IOException {
        var commentListResponse = youTubeClient.getReplies(commentId, pageToken);
        return commentListResponse.getItems()
                .stream()
                .map(commentMapper::fromYouTube)
                .toList();
    }
}

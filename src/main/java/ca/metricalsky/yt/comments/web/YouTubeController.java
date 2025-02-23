package ca.metricalsky.yt.comments.web;

import ca.metricalsky.yt.comments.client.YouTubeClient;
import ca.metricalsky.yt.comments.entity.Channel;
import ca.metricalsky.yt.comments.entity.Comment;
import ca.metricalsky.yt.comments.entity.Video;
import ca.metricalsky.yt.comments.mapper.ChannelMapper;
import ca.metricalsky.yt.comments.mapper.CommentMapper;
import ca.metricalsky.yt.comments.mapper.VideoMapper;
import ca.metricalsky.yt.comments.repository.ChannelRepository;
import ca.metricalsky.yt.comments.repository.VideoRepository;
import ca.metricalsky.yt.comments.service.CommentService;
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
    private final ChannelRepository channelRepository;
    private final VideoRepository videoRepository;
    private final CommentService commentService;

    @Autowired
    public YouTubeController(YouTubeClient youTubeClient, ChannelRepository channelRepository,
                             VideoRepository videoRepository, CommentService commentService) {
        this.youTubeClient = youTubeClient;
        this.channelRepository = channelRepository;
        this.videoRepository = videoRepository;
        this.commentService = commentService;
    }

    @GetMapping("/channels/{handle}")
    public Channel getChannelByHandle(@PathVariable String handle) throws IOException {
        var channelListResponse = youTubeClient.getChannel(handle);
        var channel = channelMapper.fromYouTube(channelListResponse.getItems().getFirst());
        channelRepository.save(channel);
        return channel;
    }

    @GetMapping("/activities/{channelId}")
    public List<Video> getActivitiesByChannelId(
            @PathVariable String channelId,
            @RequestParam(required = false) String pageToken
    ) throws IOException {
        var activityListResponse = youTubeClient.getActivities(channelId, pageToken);
        var videos = activityListResponse.getItems()
                .stream()
                .filter(activity -> activity.getContentDetails().getUpload() != null)
                .map(videoMapper::fromYouTube)
                .toList();
        videoRepository.saveAll(videos);
        return videos;
    }

    @GetMapping("/comments/{videoId}")
    public List<Comment> getCommentsByVideoId(
            @PathVariable String videoId,
            @RequestParam(required = false) String pageToken
    ) throws IOException {
        var commentThreadListResponse = youTubeClient.getComments(videoId, pageToken);
        var comments = commentThreadListResponse.getItems()
                .stream()
                .map(commentMapper::fromYouTube)
                .toList();
        commentService.saveAll(comments);
        return comments;
    }

    @GetMapping("/replies/{commentId}")
    public List<Comment> getRepliesByCommentId(
            @PathVariable String commentId,
            @RequestParam(required = false) String pageToken
    ) throws IOException {
        var commentListResponse = youTubeClient.getReplies(commentId, pageToken);
        var replies = commentListResponse.getItems()
                .stream()
                .map(commentMapper::fromYouTube)
                .toList();
        commentService.saveAll(replies);
        return replies;
    }
}

package ca.metricalsky.winston.web;

import ca.metricalsky.winston.client.YouTubeClient;
import ca.metricalsky.winston.entity.Channel;
import ca.metricalsky.winston.entity.Comment;
import ca.metricalsky.winston.mapper.entity.ChannelMapper;
import ca.metricalsky.winston.mapper.entity.CommentMapper;
import ca.metricalsky.winston.repository.ChannelRepository;
import ca.metricalsky.winston.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Deprecated
@RestController
@RequestMapping("/api/yt")
@RequiredArgsConstructor
public class YouTubeController {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    private final YouTubeClient youTubeClient;
    private final CommentService commentService;

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

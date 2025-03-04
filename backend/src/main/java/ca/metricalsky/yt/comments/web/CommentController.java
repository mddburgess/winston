package ca.metricalsky.yt.comments.web;

import ca.metricalsky.yt.comments.dto.CommentDto;
import ca.metricalsky.yt.comments.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/api/videos/{videoId}/comments")
    public List<CommentDto> listByVideoId(@PathVariable String videoId) {
        return commentService.findAllByVideoId(videoId);
    }

    @GetMapping("/api/authors/{authorId}/comments")
    public List<CommentDto> listByAuthorId(@PathVariable String authorId) {
        return commentService.findAllWithContextByAuthorId(authorId);
    }
}

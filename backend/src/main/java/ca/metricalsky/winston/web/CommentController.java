package ca.metricalsky.winston.web;

import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.service.CommentService;
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

    @GetMapping("/api/v1/videos/{videoId}/comments")
    public List<CommentDto> listByVideoId(@PathVariable String videoId) {
        return commentService.findAllByVideoId(videoId);
    }
}

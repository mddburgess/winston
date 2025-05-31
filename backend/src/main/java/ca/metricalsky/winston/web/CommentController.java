package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.CommentsApi;
import ca.metricalsky.winston.api.model.ListCommentsForVideoResponse;
import ca.metricalsky.winston.dao.CommentDataService;
import ca.metricalsky.winston.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentsApi {

    private final CommentDataService commentDataService;
    private final CommentService commentService;

    @Override
    public ResponseEntity<ListCommentsForVideoResponse> listCommentsForVideo(String id, String author) {
        var comments = commentDataService.getCommentsForVideo(id, author);
        var response = new ListCommentsForVideoResponse()
                .videoId(id)
                .comments(comments);

        return ResponseEntity.ok(response);
    }
}

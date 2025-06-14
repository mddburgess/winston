package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.CommentsApi;
import ca.metricalsky.winston.api.model.ListCommentsResponse;
import ca.metricalsky.winston.dao.CommentDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentsApi {

    private final CommentDataService commentDataService;

    @Override
    public ResponseEntity<ListCommentsResponse> listComments(String id, String author) {
        var comments = commentDataService.getCommentsForVideo(id, author);
        var response = new ListCommentsResponse()
                .videoId(id)
                .comments(comments);

        return ResponseEntity.ok(response);
    }
}

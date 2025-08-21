package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.CommentsApi;
import ca.metricalsky.winston.api.model.CommentProperties;
import ca.metricalsky.winston.api.model.ListCommentsResponse;
import ca.metricalsky.winston.api.model.PatchOperation;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.dao.CommentDataService;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.utils.JsonPatchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentsApi {

    private final CommentDataService commentDataService;
    private final JsonPatchUtils jsonPatchUtils;

    @Override
    public ResponseEntity<ListCommentsResponse> listComments(String id, String author) {
        var comments = commentDataService.getCommentsForVideo(id, author);
        var response = new ListCommentsResponse()
                .videoId(id)
                .comments(comments);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommentProperties> patchCommentProperties(String id, List<PatchOperation> patchOperations) {
        var comment = commentDataService.findCommentById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "The requested comment was not found."));

        var commentProperties = comment.getProperties();
        var patchedCommentProperties = jsonPatchUtils.applyPatch(commentProperties, patchOperations);
        comment.setProperties(patchedCommentProperties);

        commentDataService.saveCommentProperties(comment);

        return ResponseEntity.ok(patchedCommentProperties);
    }
}

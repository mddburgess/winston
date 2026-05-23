package ca.metricalsky.winston.test;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.CommentThreadSnippet;

import java.util.List;

/**
 * @deprecated Use {@link WinstonFaker} instead.
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public final class ClientTestObjectFactory {

    private static final WinstonFaker faker = new WinstonFaker();

    private ClientTestObjectFactory() {

    }

    public static CommentThreadListResponse buildCommentThreadListResponse() {
        var commentThreadListResponse = new CommentThreadListResponse();
        commentThreadListResponse.setItems(List.of(buildCommentThread()));
        return commentThreadListResponse;
    }

    private static CommentThread buildCommentThread() {
        var topLevelComment = faker.youtube().comment().minimalObject();

        var snippet = new CommentThreadSnippet();
        snippet.setTopLevelComment(topLevelComment);

        var commentThread = new CommentThread();
        commentThread.setSnippet(snippet);
        return commentThread;
    }

    public static CommentListResponse buildCommentListResponse() {
        var commentListResponse = new CommentListResponse();
        commentListResponse.setItems(List.of(faker.youtube().comment().completeObject()));
        return commentListResponse;
    }
}

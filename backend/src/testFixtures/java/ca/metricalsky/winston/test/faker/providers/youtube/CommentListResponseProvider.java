package ca.metricalsky.winston.test.faker.providers.youtube;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentListResponse;
import net.datafaker.providers.base.AbstractProvider;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class CommentListResponseProvider
        extends AbstractProvider<WinstonFaker> {

    public CommentListResponseProvider(WinstonFaker faker) {
        super(faker);
    }

    public CommentListResponse emptyPage() {
        return page(0, 0, null);
    }

    public CommentListResponse firstPage() {
        return page(1, 5, nextPageToken());
    }

    public CommentListResponse lastPage() {
        return page(1, 5, null);
    }

    public String nextPageToken() {
        var nextPageToken = faker.regexify("get_comment_with_replies_stream--[A-Za-z0-9_-]{43}");
        return Base64.getUrlEncoder().encodeToString(nextPageToken.getBytes(StandardCharsets.UTF_8));
    }

    public CommentListResponse page(int minLength, int maxLength, String nextPageToken) {
        List<Comment> comments = faker.collection(() -> faker.youtube().comment().completeReply(null))
                .minLen(minLength)
                .maxLen(maxLength)
                .generate();

        var commentListResponse = new CommentListResponse();
        commentListResponse.setItems(comments);
        commentListResponse.setNextPageToken(nextPageToken);
        return commentListResponse;
    }
}

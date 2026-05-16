package ca.metricalsky.winston.test.faker.providers.youtube;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import net.datafaker.providers.base.AbstractProvider;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class CommentThreadListResponseProvider
        extends AbstractProvider<WinstonFaker> {

    public CommentThreadListResponseProvider(WinstonFaker faker) {
        super(faker);
    }

    public CommentThreadListResponse emptyPage() {
        return page(0, 0, null);
    }

    public CommentThreadListResponse firstPage() {
        return page(1, 5, nextPageToken());
    }

    public CommentThreadListResponse lastPage() {
        return page(1, 5, null);
    }

    public String nextPageToken() {
        var nextPageToken = faker.regexify("get_newest_first--[A-Za-z0-9_-]{82}");
        return Base64.getUrlEncoder().encodeToString(nextPageToken.getBytes(StandardCharsets.UTF_8));
    }

    public CommentThreadListResponse page(int minLength, int maxLength, String nextPageToken) {
        List<CommentThread> commentThreads = faker.collection(() -> faker.youtube().commentThread())
                .minLen(minLength)
                .maxLen(maxLength)
                .generate();

        var commentThreadListResponse = new CommentThreadListResponse();
        commentThreadListResponse.setItems(commentThreads);
        commentThreadListResponse.setNextPageToken(nextPageToken);
        return commentThreadListResponse;
    }
}

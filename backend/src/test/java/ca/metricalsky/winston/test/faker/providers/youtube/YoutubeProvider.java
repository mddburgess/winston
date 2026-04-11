package ca.metricalsky.winston.test.faker.providers.youtube;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadSnippet;
import net.datafaker.providers.base.AbstractProvider;

public class YoutubeProvider
        extends AbstractProvider<WinstonFaker> {

    public YoutubeProvider(WinstonFaker faker) {
        super(faker);
    }

    public String authorId() {
        return channelId();
    }

    public String channelHandle() {
        return "@" + faker.word().noun();
    }

    public String channelId() {
        return faker.regexify("UC[A-Za-z0-9_-]{22}");
    }

    public Comment comment() {
        var comment = new Comment();
        comment.setId(commentId());
        return comment;
    }

    public String commentId() {
        return faker.regexify("Ug[A-Za-z0-9_-]{24}");
    }

    public CommentThread commentThread() {
        var topLevelComment = comment();

        var snippet = new CommentThreadSnippet();
        snippet.setTopLevelComment(topLevelComment);

        var commentThread = new CommentThread();
        commentThread.setSnippet(snippet);
        return commentThread;
    }

    public Comment reply() {
        var parentId = commentId();

        var snippet = new CommentSnippet();
        snippet.setParentId(parentId);

        var comment = new Comment();
        comment.setId(replyId(parentId));
        comment.setSnippet(snippet);
        return comment;
    }

    public String replyId() {
        return replyId(commentId());
    }

    public String replyId(String parentId) {
        return parentId + "." + faker.regexify("[A-Za-z0-9_-]{22}");
    }

    public YoutubeResponseProvider response() {
        return faker.getProvider(YoutubeResponseProvider.class, YoutubeResponseProvider::new);
    }

    public String videoId() {
        return faker.regexify("[A-Za-z0-9_-]{11}");
    }
}

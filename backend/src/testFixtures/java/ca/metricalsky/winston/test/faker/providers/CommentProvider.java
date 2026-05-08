package ca.metricalsky.winston.test.faker.providers;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.CommentText;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import net.datafaker.providers.base.AbstractProvider;

import java.util.List;

public class CommentProvider
        extends AbstractProvider<WinstonFaker> {


    public CommentProvider(WinstonFaker faker) {
        super(faker);
    }

    public Comment dto() {
        var text = faker.massEffect().quote();

        var commentText = new CommentText()
                .display(text)
                .original(text);
        return new Comment()
                .videoId(faker.youtube().videoId())
                .text(commentText);
    }

    public List<Comment> list() {
        return faker.collection(this::dto).maxLen(5).generate();
    }
}

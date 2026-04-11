package ca.metricalsky.winston.test.faker.providers;

import ca.metricalsky.winston.api.model.CommentText;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import net.datafaker.providers.base.AbstractProvider;

import java.util.List;

public class TopLevelCommentProvider
        extends AbstractProvider<WinstonFaker> {

    public TopLevelCommentProvider(WinstonFaker faker) {
        super(faker);
    }

    public TopLevelComment dto() {
        var text = faker.massEffect().quote();

        var commentText = new CommentText()
                .display(text)
                .original(text);
        return new TopLevelComment()
                .videoId(faker.youtube().videoId())
                .text(commentText);
    }

    public List<TopLevelComment> list() {
        return faker.collection(this::dto).maxLen(5).generate();
    }
}

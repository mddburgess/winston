package ca.metricalsky.winston.test.faker.providers.youtube;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import net.datafaker.providers.base.AbstractProvider;

public class YoutubeResponseProvider
        extends AbstractProvider<WinstonFaker> {

    public YoutubeResponseProvider(WinstonFaker faker) {
        super(faker);
    }

    public CommentListResponseProvider commentList() {
        return faker.getProvider(CommentListResponseProvider.class, CommentListResponseProvider::new);
    }
}

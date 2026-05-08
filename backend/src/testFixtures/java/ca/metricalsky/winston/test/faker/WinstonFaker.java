package ca.metricalsky.winston.test.faker;

import ca.metricalsky.winston.database.test.faker.DatabaseFaker;
import ca.metricalsky.winston.test.faker.providers.CommentProvider;
import ca.metricalsky.winston.test.faker.providers.PageProvider;
import ca.metricalsky.winston.test.faker.providers.TopLevelCommentProvider;
import ca.metricalsky.winston.test.faker.providers.VideoProvider;
import ca.metricalsky.winston.test.faker.providers.youtube.YoutubeProvider;
import net.datafaker.Faker;

public class WinstonFaker extends Faker {

    private static final DatabaseFaker database = new DatabaseFaker();

    public CommentProvider comment() {
        return getProvider(CommentProvider.class, CommentProvider::new);
    }

    public DatabaseFaker database() {
        return database;
    }

    public PageProvider page() {
        return getProvider(PageProvider.class, PageProvider::new);
    }

    public TopLevelCommentProvider topLevelComment() {
        return getProvider(TopLevelCommentProvider.class, TopLevelCommentProvider::new);
    }

    public VideoProvider video() {
        return getProvider(VideoProvider.class, VideoProvider::new);
    }

    public YoutubeProvider youtube() {
        return getProvider(YoutubeProvider.class, YoutubeProvider::new);
    }
}

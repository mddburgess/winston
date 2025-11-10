package ca.metricalsky.winston.test.faker;

import ca.metricalsky.winston.test.faker.providers.PageProvider;
import ca.metricalsky.winston.test.faker.providers.VideoProvider;
import ca.metricalsky.winston.test.faker.providers.YoutubeProvider;
import ca.metricalsky.winston.test.faker.providers.entity.AuthorEntityProvider;
import net.datafaker.Faker;

public class WinstonFaker extends Faker {

    public AuthorEntityProvider authorEntity() {
        return getProvider(AuthorEntityProvider.class, AuthorEntityProvider::new);
    }

    public PageProvider page() {
        return getProvider(PageProvider.class, PageProvider::new);
    }

    public VideoProvider video() {
        return getProvider(VideoProvider.class, VideoProvider::new);
    }

    public YoutubeProvider youtube() {
        return getProvider(YoutubeProvider.class, YoutubeProvider::new);
    }
}

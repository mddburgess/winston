package ca.metricalsky.winston.test.faker.providers;

import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

public class YoutubeProvider extends AbstractProvider<BaseProviders> {

    public YoutubeProvider(BaseProviders faker) {
        super(faker);
    }

    public String channelHandle() {
        return "@" + faker.word().noun();
    }

    public String channelId() {
        return faker.regexify("UC[A-Za-z0-9_-]{22}");
    }

    public String videoId() {
        return faker.regexify("[A-Za-z0-9_-]{11}");
    }
}

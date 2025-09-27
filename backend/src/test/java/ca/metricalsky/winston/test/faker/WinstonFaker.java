package ca.metricalsky.winston.test.faker;

import ca.metricalsky.winston.test.faker.providers.Page;
import ca.metricalsky.winston.test.faker.providers.Youtube;
import net.datafaker.Faker;

public class WinstonFaker extends Faker {

    public Page page() {
        return getProvider(Page.class, Page::new);
    }

    public Youtube youtube() {
        return getProvider(Youtube.class, Youtube::new);
    }
}

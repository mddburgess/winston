package ca.metricalsky.winston.test.faker;

import net.datafaker.Faker;

public class WinstonFaker extends Faker {

    public Youtube youtube() {
        return getProvider(Youtube.class, Youtube::new);
    }
}

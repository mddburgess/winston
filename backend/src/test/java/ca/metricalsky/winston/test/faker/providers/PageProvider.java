package ca.metricalsky.winston.test.faker.providers;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import net.datafaker.providers.base.AbstractProvider;
import org.springframework.data.domain.PageRequest;

public class PageProvider
        extends AbstractProvider<WinstonFaker> {

    public PageProvider(WinstonFaker faker) {
        super(faker);
    }

    public PageRequest pageRequest() {
        return PageRequest.of(pageNumber(), pageSize());
    }

    public int pageNumber() {
        return faker.number().numberBetween(0, 100);
    }

    public int pageSize() {
        return faker.number().numberBetween(1, 500);
    }
}

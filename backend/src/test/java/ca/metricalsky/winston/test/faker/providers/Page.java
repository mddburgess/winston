package ca.metricalsky.winston.test.faker.providers;

import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;
import org.springframework.data.domain.PageRequest;

public class Page extends AbstractProvider<BaseProviders> {

    public Page(BaseProviders faker) {
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

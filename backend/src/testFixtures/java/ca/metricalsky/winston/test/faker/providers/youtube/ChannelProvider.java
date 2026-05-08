package ca.metricalsky.winston.test.faker.providers.youtube;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.google.api.services.youtube.model.Channel;
import net.datafaker.providers.base.AbstractProvider;

public class ChannelProvider
extends AbstractProvider<WinstonFaker> {

    protected ChannelProvider(WinstonFaker faker) {
        super(faker);
    }

    public String id() {
        return faker.regexify("UC[A-Za-z0-9_-]{22}");
    }

    public Channel minimalObject() {
        var channel = new Channel();
        channel.setId(id());
        return channel;
    }
}

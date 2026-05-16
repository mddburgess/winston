package ca.metricalsky.winston.test.faker.providers.youtube;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.google.api.services.youtube.model.ChannelListResponse;
import net.datafaker.providers.base.AbstractProvider;

import java.util.List;

public class ChannelListResponseProvider
        extends AbstractProvider<WinstonFaker> {

    public ChannelListResponseProvider(WinstonFaker faker) {
        super(faker);
    }

    public ChannelListResponse emptyPage() {
        return new ChannelListResponse();
    }

    public ChannelListResponse channelFound() {
        var channelListResponse = new ChannelListResponse();
        channelListResponse.setItems(List.of(faker.youtube().channel().minimalObject()));
        return channelListResponse;
    }
}

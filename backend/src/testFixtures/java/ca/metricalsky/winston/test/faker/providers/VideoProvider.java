package ca.metricalsky.winston.test.faker.providers;

import ca.metricalsky.winston.api.model.ChannelSummary;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import net.datafaker.providers.base.AbstractProvider;

public class VideoProvider
        extends AbstractProvider<WinstonFaker> {

    public VideoProvider(WinstonFaker faker) {
        super(faker);
    }

    public Video dto() {
        return dto(faker.youtube().channel().handle());
    }

    public Video dto(String channelHandle) {
        var channel = new ChannelSummary()
                .handle(channelHandle);

        return new Video()
                .id(faker.youtube().videoId())
                .channel(channel);
    }
}

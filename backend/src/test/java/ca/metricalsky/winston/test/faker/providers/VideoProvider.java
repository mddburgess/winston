package ca.metricalsky.winston.test.faker.providers;

import ca.metricalsky.winston.api.model.ChannelSummary;
import ca.metricalsky.winston.api.model.Video;
import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

public class VideoProvider extends AbstractProvider<BaseProviders> {

    private final YoutubeProvider youtube;

    public VideoProvider(BaseProviders faker) {
        super(faker);
        youtube = new YoutubeProvider(faker);
    }

    public Video dto() {
        return dto(youtube.channelHandle());
    }

    public Video dto(String channelHandle) {
        var channel = new ChannelSummary()
                .handle(channelHandle);

        return new Video()
                .id(youtube.videoId())
                .channel(channel);
    }
}

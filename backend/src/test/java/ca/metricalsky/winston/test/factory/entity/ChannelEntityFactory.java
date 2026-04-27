package ca.metricalsky.winston.test.factory.entity;

import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.database.entity.channel.ChannelPropertiesEntity;
import ca.metricalsky.winston.test.faker.WinstonFaker;

import java.time.ZoneOffset;
import java.util.Set;

public final class ChannelEntityFactory {

    private static final WinstonFaker faker = new WinstonFaker();

    private ChannelEntityFactory() {

    }

    public static ChannelEntity createChannelEntity() {
        var channelId = faker.youtube().channelId();
        var channelProperties = new ChannelPropertiesEntity(channelId, faker.bool().bool());

        return new ChannelEntity()
                .setId(channelId)
                .setTitle(faker.massEffect().character())
                .setDescription(faker.massEffect().quote())
                .setCustomUrl("@" + faker.name().firstName().toLowerCase())
                .setThumbnailUrl(faker.internet().url())
                .setUploadsPlaylistId(faker.internet().uuid())
                .setVideoCount((long) faker.number().positive())
                .setViewCount((long) faker.number().positive())
                .setSubscriberCount((long) faker.number().positive())
                .setTopics(Set.of(faker.internet().url()))
                .setKeywords(Set.of(faker.massEffect().planet()))
                .setPublishedAt(faker.timeAndDate().past().atOffset(ZoneOffset.UTC))
                .setLastFetchedAt(faker.timeAndDate().past().atOffset(ZoneOffset.UTC))
                .setProperties(channelProperties);
    }
}

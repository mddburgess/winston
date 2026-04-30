package ca.metricalsky.winston.test.factory.entity;

import ca.metricalsky.winston.database.entity.channel.ChannelEntity;
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

        var channelEntity = new ChannelEntity();
        channelEntity.setId(channelId);
        channelEntity.setTitle(faker.massEffect().character());
        channelEntity.setDescription(faker.massEffect().quote());
        channelEntity.setCustomUrl("@" + faker.name().firstName().toLowerCase());
        channelEntity.setThumbnailUrl(faker.internet().url());
        channelEntity.setUploadsPlaylistId(faker.internet().uuid());
        channelEntity.setVideoCount((long) faker.number().positive());
        channelEntity.setViewCount((long) faker.number().positive());
        channelEntity.setSubscriberCount((long) faker.number().positive());
        channelEntity.setTopics(Set.of(faker.internet().url()));
        channelEntity.setKeywords(Set.of(faker.massEffect().planet()));
        channelEntity.setPublishedAt(faker.timeAndDate().past().atOffset(ZoneOffset.UTC));
        channelEntity.setLastFetchedAt(faker.timeAndDate().past().atOffset(ZoneOffset.UTC));
        channelEntity.setProperties(channelProperties);
        return channelEntity;
    }
}

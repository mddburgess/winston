package ca.metricalsky.winston.database.test.faker.providers.video

import ca.metricalsky.winston.database.entity.channel.ChannelEntity
import ca.metricalsky.winston.database.entity.video.VideoEntity
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import net.datafaker.providers.base.AbstractProvider
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class VideoEntityProvider(faker: DatabaseFaker): AbstractProvider<DatabaseFaker>(faker) {

    fun id(): String = faker.regexify("[A-Za-z0-9_-]{11}")

    fun minimalEntity(channel: ChannelEntity? = null) = VideoEntity(
        id = id(),
        channelId = channel?.id,
    )

    fun completeEntity(channel: ChannelEntity? = null) = VideoEntity(
        id = id(),
        channelId = channel?.id,
        title = faker.massEffect().character(),
        description = faker.massEffect().quote(),
        thumbnailUrl = faker.internet().url(),
        publishedAt = faker.timeAndDate().past(1, TimeUnit.DAYS).atOffset(ZoneOffset.UTC),
    )
}

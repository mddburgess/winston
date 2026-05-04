package ca.metricalsky.winston.database.test.faker.providers.channel

import ca.metricalsky.winston.database.entity.channel.ChannelEntity
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import ca.metricalsky.winston.database.test.faker.ext.generateSet
import net.datafaker.providers.base.AbstractProvider
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class ChannelEntityProvider(faker: DatabaseFaker) : AbstractProvider<DatabaseFaker>(faker) {

    fun id(): String = faker.regexify("UC[A-Za-z0-9_-]{22}")

    fun minimalEntity() = ChannelEntity(
        id = id(),
    )

    fun completeEntity() = ChannelEntity(
        id = id(),
        title = faker.massEffect().character(),
        description = faker.massEffect().quote(),
        customUrl = faker.internet().url(),
        thumbnailUrl = faker.internet().url(),
        uploadsPlaylistId = faker.internet().uuidv4(),
        videoCount = faker.number().numberBetween(0L, 10_000L),
        viewCount = faker.number().numberBetween(0L, 1_000_000L),
        subscriberCount = faker.number().numberBetween(0L, 1_000_000L),
        publishedAt = faker.timeAndDate().past(10, TimeUnit.DAYS).atOffset(ZoneOffset.UTC),
        topics = faker.collection({ faker.internet().url() }).minLen(1).maxLen(5).generateSet(),
        keywords = faker.collection({ faker.word().noun() }).minLen(1).maxLen(5).generateSet()
    )
}

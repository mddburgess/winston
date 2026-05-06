package ca.metricalsky.winston.database.test.faker.providers.fetch

import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity.Type
import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import net.datafaker.providers.base.AbstractProvider
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class FetchOperationEntityProvider(faker: DatabaseFaker): AbstractProvider<DatabaseFaker>(faker) {

    fun minimalEntity(fetchRequest: FetchRequestEntity? = null) = FetchOperationEntity(
        fetchRequestId = fetchRequest?.id,
        operationType = Type.CHANNELS,
        objectId = faker.internet().uuidv4(),
    )

    fun completeEntity(fetchRequest: FetchRequestEntity? = null) = FetchOperationEntity(
        fetchRequestId = fetchRequest?.id,
        operationType = Type.CHANNELS,
        objectId = faker.internet().uuidv4(),
        mode = faker.word().noun(),
        publishedAfter = faker.timeAndDate().past(7, TimeUnit.DAYS).atOffset(ZoneOffset.UTC),
        publishedBefore = OffsetDateTime.now(ZoneOffset.UTC),
    )

    fun channels(fetchRequest: FetchRequestEntity) = FetchOperationEntity(
        fetchRequestId = fetchRequest.id,
        operationType = Type.CHANNELS,
        objectId = faker.channel().handle(),
    )

    fun comments(fetchRequest: FetchRequestEntity) = FetchOperationEntity(
        fetchRequestId = fetchRequest.id,
        operationType = Type.COMMENTS,
        objectId = faker.video().id(),
    )

    fun replies(fetchRequest: FetchRequestEntity) = FetchOperationEntity(
        fetchRequestId = fetchRequest.id,
        operationType = Type.REPLIES,
        objectId = faker.comment().id(),
    )

    fun videos(fetchRequest: FetchRequestEntity) = FetchOperationEntity(
        fetchRequestId = fetchRequest.id,
        operationType = Type.VIDEOS,
        objectId = faker.channel().id(),
    )
}

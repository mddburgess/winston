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

    fun minimalEntity() = FetchOperationEntity(
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
}

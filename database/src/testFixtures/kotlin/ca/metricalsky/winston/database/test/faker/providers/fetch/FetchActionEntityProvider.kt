package ca.metricalsky.winston.database.test.faker.providers.fetch

import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity.Type
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import net.datafaker.providers.base.AbstractProvider
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class FetchActionEntityProvider(faker: DatabaseFaker): AbstractProvider<DatabaseFaker>(faker) {

    fun minimalEntity(fetchOperation: FetchOperationEntity? = null) = FetchActionEntity(
        fetchOperationId = fetchOperation?.id,
        actionType = Type.CHANNELS,
        objectId = faker.internet().uuidv4(),
    )

    fun completeEntity(fetchOperation: FetchOperationEntity? = null) = FetchActionEntity(
        fetchOperationId = fetchOperation?.id,
        actionType = Type.CHANNELS,
        objectId = faker.internet().uuidv4(),
        publishedAfter = faker.timeAndDate().past(7, TimeUnit.DAYS).atOffset(ZoneOffset.UTC),
        publishedBefore = OffsetDateTime.now(ZoneOffset.UTC),
    )
}

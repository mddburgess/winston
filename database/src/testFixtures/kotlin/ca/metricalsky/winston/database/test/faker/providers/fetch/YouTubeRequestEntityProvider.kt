package ca.metricalsky.winston.database.test.faker.providers.fetch

import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity
import ca.metricalsky.winston.database.entity.fetch.YouTubeRequestEntity
import ca.metricalsky.winston.database.entity.fetch.YouTubeRequestEntity.RequestType
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import net.datafaker.providers.base.AbstractProvider

class YouTubeRequestEntityProvider(faker: DatabaseFaker): AbstractProvider<DatabaseFaker>(faker) {

    fun minimalEntity(fetchAction: FetchActionEntity? = null) = YouTubeRequestEntity(
        fetchActionId = fetchAction?.id,
        requestType = RequestType.CHANNELS,
        objectId = faker.internet().uuidv4(),
    )
}

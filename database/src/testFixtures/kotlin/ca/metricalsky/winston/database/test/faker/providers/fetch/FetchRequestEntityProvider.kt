package ca.metricalsky.winston.database.test.faker.providers.fetch

import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import net.datafaker.providers.base.AbstractProvider

class FetchRequestEntityProvider(faker: DatabaseFaker): AbstractProvider<DatabaseFaker>(faker) {

    fun minimalEntity() = FetchRequestEntity()
}

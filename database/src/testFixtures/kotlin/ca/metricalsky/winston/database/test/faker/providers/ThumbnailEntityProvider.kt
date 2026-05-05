package ca.metricalsky.winston.database.test.faker.providers

import ca.metricalsky.winston.database.entity.ThumbnailEntity
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import net.datafaker.providers.base.AbstractProvider

class ThumbnailEntityProvider(faker: DatabaseFaker): AbstractProvider<DatabaseFaker>(faker) {

    fun completeEntity() = ThumbnailEntity(
        id = faker.internet().uuidv4(),
        url = faker.internet().url(),
        image = faker.random().nextRandomBytes(1000)
    )
}

package ca.metricalsky.winston.database.test.faker.providers

import ca.metricalsky.winston.database.entity.author.AuthorEntity
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import net.datafaker.providers.base.AbstractProvider

class AuthorEntityProvider(faker: DatabaseFaker) : AbstractProvider<DatabaseFaker>(faker) {

    fun minimal(): AuthorEntity {
        return AuthorEntity(
            id = faker.internet().uuidv4()
        )
    }

    fun complete(): AuthorEntity {
        return AuthorEntity(
            id = faker.internet().uuidv4(),
            displayName = "@" + faker.name().firstName(),
            channelUrl = faker.internet().url(),
            profileImageUrl = faker.internet().url(),
            aliases = setOf("@" + faker.name().firstName()),
        )
    }
}

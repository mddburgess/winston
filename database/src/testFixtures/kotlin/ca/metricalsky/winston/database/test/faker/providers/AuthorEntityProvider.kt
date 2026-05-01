package ca.metricalsky.winston.database.test.faker.providers

import ca.metricalsky.winston.database.entity.author.AuthorEntity
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import net.datafaker.providers.base.AbstractProvider

class AuthorEntityProvider(faker: DatabaseFaker) : AbstractProvider<DatabaseFaker>(faker) {

    fun id(): String = faker.regexify("UC[A-Za-z0-9_-]{22}")

    fun minimalEntity() = AuthorEntity(
        id = id(),
    )

    fun completeEntity() = AuthorEntity(
        id = id(),
        displayName = "@" + faker.name().firstName(),
        channelUrl = faker.internet().url(),
        profileImageUrl = faker.internet().url(),
        aliases = setOf("@" + faker.name().firstName()),
    )
}

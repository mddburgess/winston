package ca.metricalsky.winston.database.test.faker.providers

import net.datafaker.providers.base.ProviderRegistration

interface DatabaseProviders : ProviderRegistration {

    fun author() = getProvider(AuthorEntityProvider::class.java) {
        AuthorEntityProvider(it)
    }

    fun channel() = getProvider(ChannelEntityProvider::class.java) {
        ChannelEntityProvider(it)
    }
}

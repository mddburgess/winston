package ca.metricalsky.winston.database.test.faker.providers

import ca.metricalsky.winston.database.test.faker.providers.fetch.FetchActionEntityProvider
import ca.metricalsky.winston.database.test.faker.providers.fetch.FetchOperationEntityProvider
import ca.metricalsky.winston.database.test.faker.providers.fetch.FetchRequestEntityProvider
import net.datafaker.providers.base.ProviderRegistration

interface DatabaseProviders : ProviderRegistration {

    fun author() = getProvider(AuthorEntityProvider::class.java) {
        AuthorEntityProvider(it)
    }

    fun channel() = getProvider(ChannelEntityProvider::class.java) {
        ChannelEntityProvider(it)
    }

    fun fetchAction() = getProvider(FetchActionEntityProvider::class.java) {
        FetchActionEntityProvider(it)
    }

    fun fetchOperation() = getProvider(FetchOperationEntityProvider::class.java) {
        FetchOperationEntityProvider(it)
    }

    fun fetchRequest() = getProvider(FetchRequestEntityProvider::class.java) {
        FetchRequestEntityProvider(it)
    }
}

package ca.metricalsky.winston.test.faker.providers

import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import ca.metricalsky.winston.test.faker.providers.youtube.YoutubeProvider
import net.datafaker.providers.base.ProviderRegistration


interface WinstonProviders: ProviderRegistration {

    fun comment() = getProvider(CommentProvider::class.java) {
        CommentProvider(it)
    }

    fun database() = DatabaseFaker()

    fun page() = getProvider(PageProvider::class.java) {
        PageProvider(it)
    }

    fun topLevelComment() = getProvider(TopLevelCommentProvider::class.java) {
        TopLevelCommentProvider(it)
    }

    fun video() = getProvider(VideoProvider::class.java) {
        VideoProvider(it)
    }

    fun youtube() = getProvider(YoutubeProvider::class.java) {
        YoutubeProvider(it)
    }
}

package ca.metricalsky.winston.test.faker.providers.youtube

import ca.metricalsky.winston.test.faker.WinstonFaker
import com.google.api.services.youtube.model.Comment
import com.google.api.services.youtube.model.CommentSnippet
import net.datafaker.providers.base.AbstractProvider

class YoutubeProvider(faker: WinstonFaker): AbstractProvider<WinstonFaker>(faker) {

    fun activity() = faker.getProvider(ActivityProvider::class.java) {
        ActivityProvider(it)
    }

    fun channel() = faker.getProvider(ChannelProvider::class.java) {
        ChannelProvider(it)
    }

    fun comment() = faker.getProvider(CommentProvider::class.java) {
        CommentProvider(it)
    }

    fun commentThread() = faker.getProvider(CommentThreadProvider::class.java) {
        CommentThreadProvider(it)
    }

    fun playlistId() = faker.regexify("UU[A-Za-z0-9_-]{22}")

    fun response() = faker.getProvider(YoutubeResponseProvider::class.java) {
            YoutubeResponseProvider(it)
    }

    fun videoId() = faker.regexify("[A-Za-z0-9_-]{11}")
}

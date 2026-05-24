package ca.metricalsky.winston.test.faker.providers.youtube

import ca.metricalsky.winston.test.faker.WinstonFaker
import com.google.api.services.youtube.model.CommentThread
import com.google.api.services.youtube.model.CommentThreadReplies
import com.google.api.services.youtube.model.CommentThreadSnippet
import net.datafaker.providers.base.AbstractProvider

class CommentThreadProvider(faker: WinstonFaker): AbstractProvider<WinstonFaker>(faker) {

    fun completeObject() = CommentThread().apply {
        snippet = CommentThreadSnippet().apply {
            topLevelComment = faker.youtube().comment().completeComment()
            totalReplyCount = faker.number().numberBetween(1L, 100L)
        }
        replies = CommentThreadReplies().apply {
            comments = listOf(faker.youtube().comment().completeReply(snippet.topLevelComment))
        }
    }
}

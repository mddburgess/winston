package ca.metricalsky.winston.test.faker.providers.youtube

import ca.metricalsky.winston.test.faker.WinstonFaker
import com.google.api.client.util.DateTime
import com.google.api.services.youtube.model.Comment
import com.google.api.services.youtube.model.CommentSnippet
import com.google.api.services.youtube.model.CommentSnippetAuthorChannelId
import net.datafaker.providers.base.AbstractProvider

class CommentProvider(faker: WinstonFaker) : AbstractProvider<WinstonFaker>(faker) {

    fun id() = faker.regexify("Ug[A-Za-z0-9_-]{24}")

    fun replyId(parentId: String? = id()) = parentId + "." + faker.regexify("[A-Za-z0-9_-]{22}")

    fun minimalObject() = Comment().apply {
        id = id()
    }

    fun completeComment() = Comment().apply {
        id = id()
        snippet = CommentSnippet().apply {
            videoId = faker.youtube().videoId()
            textDisplay = faker.lorem().sentence()
            textOriginal = faker.lorem().sentence()
            authorDisplayName = faker.youtube().channel().handle()
            authorProfileImageUrl = faker.internet().url()
            authorChannelUrl = faker.internet().url()
            authorChannelId = CommentSnippetAuthorChannelId().apply {
                value = faker.youtube().channel().id()
            }
            likeCount = faker.number().numberBetween(0L, 100L)
            publishedAt = DateTime.parseRfc3339("2025-01-01T00:00:00Z")
            updatedAt = DateTime.parseRfc3339("2025-01-01T00:00:00Z")
        }
    }

    fun completeReply(parent: Comment? = null) = completeComment().apply {
        snippet.parentId = parent?.id ?: id
        id = replyId(snippet.parentId)
    }
}

package ca.metricalsky.winston.test.faker.providers.youtube

import ca.metricalsky.winston.test.faker.WinstonFaker
import com.google.api.services.youtube.model.Comment
import com.google.api.services.youtube.model.CommentSnippet
import com.google.api.services.youtube.model.CommentThread
import com.google.api.services.youtube.model.CommentThreadSnippet
import net.datafaker.providers.base.AbstractProvider

class YoutubeProvider(faker: WinstonFaker): AbstractProvider<WinstonFaker>(faker) {

    fun activity() = faker.getProvider(ActivityProvider::class.java) {
        ActivityProvider(it)
    }

    fun channel() = faker.getProvider(ChannelProvider::class.java) {
        ChannelProvider(it)
    }

    @Deprecated(
        "Use channel().handle() instead.",
        ReplaceWith("channel().handle()")
    )
    fun channelHandle() = channel().handle()

    fun comment(): Comment {
        val comment = Comment()
        comment.setId(commentId())
        return comment
    }

    fun commentId() = faker.regexify("Ug[A-Za-z0-9_-]{24}")

    fun commentThread(): CommentThread {
        val topLevelComment = comment()

        val snippet = CommentThreadSnippet()
        snippet.setTopLevelComment(topLevelComment)

        val commentThread = CommentThread()
        commentThread.setSnippet(snippet)
        return commentThread
    }

    fun playlistId() = faker.regexify("UU[A-Za-z0-9_-]{22}")

    fun reply(): Comment {
        val parentId = commentId()

        val snippet = CommentSnippet()
        snippet.setParentId(parentId)

        val comment = Comment()
        comment.setId(replyId(parentId))
        comment.setSnippet(snippet)
        return comment
    }

    fun replyId(parentId: String? = commentId()) = parentId + "." + faker.regexify("[A-Za-z0-9_-]{22}")

    fun response() = faker.getProvider(YoutubeResponseProvider::class.java) {
            YoutubeResponseProvider(it)
    }

    fun videoId() = faker.regexify("[A-Za-z0-9_-]{11}")
}

package ca.metricalsky.winston.database.view

import java.time.OffsetDateTime

interface AuthorChannelView {

    fun getChannelTitle(): String

    fun getChannelHandle(): String

    fun getVideoCount(): Long

    fun getTotalCommentCount(): Long

    fun getReplyCount(): Long

    fun getFirstCommentedAt(): OffsetDateTime

    fun getLastCommentedAt(): OffsetDateTime

    fun getCommentCount(): Long {
        return getTotalCommentCount() - getReplyCount()
    }
}

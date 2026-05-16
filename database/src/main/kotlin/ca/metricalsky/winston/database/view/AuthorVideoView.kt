package ca.metricalsky.winston.database.view

import java.time.OffsetDateTime

interface AuthorVideoView {

    fun getVideoId(): String

    fun getVideoTitle(): String

    fun getVideoThumbnailUrl(): String

    fun getTotalCommentCount(): Long

    fun getReplyCount(): Long

    fun getFirstCommentedAt(): OffsetDateTime

    fun getLastCommentedAt(): OffsetDateTime

    fun getCommentCount(): Long {
        return getTotalCommentCount() - getReplyCount()
    }
}

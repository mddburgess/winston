package ca.metricalsky.winston.database.view

import java.time.OffsetDateTime

interface VideoStatisticsView {

    fun getChannelId(): String

    fun getVideoId(): String

    fun getCommentCount(): Long

    fun getReplyCount(): Long

    fun getLastCommentedAt(): OffsetDateTime
}

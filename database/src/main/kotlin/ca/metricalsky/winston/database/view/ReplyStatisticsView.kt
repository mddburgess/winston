package ca.metricalsky.winston.database.view

import java.time.OffsetDateTime

interface ReplyStatisticsView {

    val commentId: String
    val commentPublishedAt: OffsetDateTime
    val commentReplyCount: Int
    val commentLastFetchedAt: OffsetDateTime
    val fetchedReplyCount: Int?
    val mostRecentReplyPublishedAt: OffsetDateTime?
}

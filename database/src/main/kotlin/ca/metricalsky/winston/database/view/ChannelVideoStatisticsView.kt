package ca.metricalsky.winston.database.view

import java.time.OffsetDateTime

interface ChannelVideoStatisticsView {

    val channelHandle: String
    val channelId: String
    val channelPublishedAt: OffsetDateTime
    val channelVideoCount: Int?
    val videoCount: Int
    val latestVideoPublishedAt: OffsetDateTime
}

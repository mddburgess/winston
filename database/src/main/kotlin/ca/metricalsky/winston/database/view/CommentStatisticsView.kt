package ca.metricalsky.winston.database.view

interface CommentStatisticsView {

    fun getVideoCount(): Long

    fun getCommentCount(): Long

    fun getReplyCount(): Long
}

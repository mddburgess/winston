package ca.metricalsky.winston.database.view

interface CommentCountView {

    val videoId: String
    val comments: Long
        get() = this.commentsAndReplies - this.replies
    val commentsAndReplies: Long
    val replies: Long
    val totalReplies: Long
}

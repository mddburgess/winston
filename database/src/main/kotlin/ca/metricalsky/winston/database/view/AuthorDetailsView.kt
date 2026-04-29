package ca.metricalsky.winston.database.view

interface AuthorDetailsView {

    fun getAuthorId(): String

    fun getChannelCount(): Long

    fun getVideoCount(): Long

    fun getTotalCommentCount(): Long

    fun getReplyCount(): Long

    fun getCommentCount(): Long {
        return getTotalCommentCount() - getReplyCount()
    }
}

package ca.metricalsky.winston.service

import ca.metricalsky.winston.database.entity.video.VideoCommentsEntity
import ca.metricalsky.winston.database.entity.video.VideoCommentsEntity.CommentStatus
import ca.metricalsky.winston.database.repository.comment.CommentRepository
import ca.metricalsky.winston.database.repository.video.VideoCommentsRepository
import ca.metricalsky.winston.utils.ext.orElse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VideoCommentsService(
    private val commentRepository: CommentRepository,
    private val videoCommentsRepository: VideoCommentsRepository,
) {

    @Transactional
    fun updateVideoComments(videoId: String) {
        val counts = commentRepository.countCommentsForVideoId(videoId)
        val videoComments = (videoCommentsRepository.findById(videoId) orElse VideoCommentsEntity(videoId)).also {
            it.commentCount = counts.comments
            it.replyCount = counts.replies
            it.totalReplyCount = counts.totalReplies
        }
        videoCommentsRepository.save(videoComments)
    }

    @Transactional
    fun markVideoCommentStatus(videoId: String, commentStatus: CommentStatus) {
        val videoComments = (videoCommentsRepository.findById(videoId) orElse VideoCommentsEntity(videoId)).also {
            it.commentStatus = commentStatus
        }
        videoCommentsRepository.save(videoComments)
    }
}

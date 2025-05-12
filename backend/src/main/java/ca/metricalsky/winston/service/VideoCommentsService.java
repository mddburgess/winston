package ca.metricalsky.winston.service;

import ca.metricalsky.winston.entity.VideoCommentsEntity;
import ca.metricalsky.winston.repository.VideoCommentsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoCommentsService {

    private final CommentService commentService;
    private final VideoCommentsRepository videoCommentsRepository;

    @Transactional
    public void updateVideoComments(String videoId) {
        var videoComments = videoCommentsRepository.findById(videoId)
                .orElse(new VideoCommentsEntity());
        var commentCount = commentService.getCommentCountByVideoId(videoId);

        videoComments.setVideoId(videoId);
        videoComments.setCommentCount(commentCount.getComments());
        videoComments.setReplyCount(commentCount.getReplies());
        videoComments.setTotalReplyCount(commentCount.getTotalReplies());

        videoCommentsRepository.save(videoComments);
    }

    @Transactional
    public void markVideoCommentsDisabled(String videoId) {
        var videoComments = videoCommentsRepository.findById(videoId)
                .orElse(new VideoCommentsEntity());

        videoComments.setVideoId(videoId);
        videoComments.setCommentsDisabled(true);

        videoCommentsRepository.save(videoComments);
    }
}

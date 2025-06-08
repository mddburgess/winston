package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.entity.CommentEntity;
import ca.metricalsky.winston.mappers.api.CommentMapper;
import ca.metricalsky.winston.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentDataService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public List<TopLevelComment> getCommentsForVideo(String videoId, String authorHandle) {
        List<CommentEntity> comments;
        if (authorHandle != null) {
            comments = commentRepository.findCommentsForVideoByAuthor(videoId, authorHandle);
        } else {
            comments = commentRepository.findCommentsForVideo(videoId);
        }
        return comments.stream()
                .map(commentMapper::toTopLevelComment)
                .toList();
    }
}

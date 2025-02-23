package ca.metricalsky.yt.comments.service;

import ca.metricalsky.yt.comments.entity.Comment;
import ca.metricalsky.yt.comments.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void saveCommentsAndReplies(List<Comment> comments) {
        var replies = comments.stream()
                .map(CommentService::detachReplies)
                .flatMap(List::stream)
                .toList();
        var commentsAndReplies = ListUtils.union(comments, replies);
        commentRepository.saveAll(commentsAndReplies);
    }

    private static List<Comment> detachReplies(Comment comment) {
        if (comment.getReplies() == null) {
            return List.of();
        }

        var replies = List.copyOf(comment.getReplies());
        comment.setReplies(null);
        return replies;
    }
}

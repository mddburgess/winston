package ca.metricalsky.yt.comments.service;

import ca.metricalsky.yt.comments.entity.Author;
import ca.metricalsky.yt.comments.entity.Comment;
import ca.metricalsky.yt.comments.repository.AuthorRepository;
import ca.metricalsky.yt.comments.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, AuthorRepository authorRepository) {
        this.commentRepository = commentRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public void saveAll(List<Comment> comments) {
        var authors = getAuthors(comments);
        authorRepository.saveAll(authors);
        commentRepository.saveAll(comments);
    }

    private static List<Author> getAuthors(List<Comment> comments) {
        return comments.stream()
                .map(CommentService::getCommentAndReplies)
                .flatMap(List::stream)
                .map(Comment::getAuthor)
                .toList();
    }

    private static List<Comment> getCommentAndReplies(Comment comment) {
        return comment.getReplies() != null
                ? ListUtils.union(List.of(comment), comment.getReplies())
                : List.of(comment);
    }
}

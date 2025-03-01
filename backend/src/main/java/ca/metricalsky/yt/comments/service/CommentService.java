package ca.metricalsky.yt.comments.service;

import ca.metricalsky.yt.comments.dto.CommentDto;
import ca.metricalsky.yt.comments.entity.Author;
import ca.metricalsky.yt.comments.entity.Comment;
import ca.metricalsky.yt.comments.mapper.CommentMapper;
import ca.metricalsky.yt.comments.repository.AuthorRepository;
import ca.metricalsky.yt.comments.repository.CommentRepository;
import jakarta.persistence.Tuple;
import org.apache.commons.collections4.ListUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.firstNonNull;
import static org.apache.commons.collections4.map.DefaultedMap.defaultedMap;

@Service
public class CommentService {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

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

    public Map<String, Count> getCommentCountsByChannelId(String channelId) {
        var counts = commentRepository.countCommentsForChannelIdGroupByVideoId(channelId)
                .stream()
                .collect(Collectors.toMap(tuple -> tuple.get(0, String.class), Count::forComment));
        return defaultedMap(counts, Count.EMPTY);
    }

    public Map<String, Count> getReplyCountsByChannelId(String channelId) {
        var counts = commentRepository.countRepliesForChannelIdGroupByVideoId(channelId)
                .stream()
                .collect(Collectors.toMap(tuple -> tuple.get(0, String.class), Count::forReply));
        return defaultedMap(counts, Count.EMPTY);
    }

    public List<CommentDto> findAllByVideoId(String videoId) {
        return commentRepository.findAllByVideoId(videoId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public record Count(Long comments, Long replies) {

        private static final Count EMPTY = new Count(0L, 0L);

        private static Count forComment(Tuple comment) {
            return new Count(
                    firstNonNull(comment.get(1, Long.class), 0L),
                    firstNonNull(comment.get(2, Long.class), 0L)
            );
        }

        private static Count forReply(Tuple reply) {
            return new Count(
                    firstNonNull(reply.get(1, Long.class), 0L),
                    0L
            );
        }
    }
}

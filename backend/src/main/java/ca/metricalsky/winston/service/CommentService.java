package ca.metricalsky.winston.service;

import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.entity.Author;
import ca.metricalsky.winston.entity.Comment;
import ca.metricalsky.winston.entity.view.CommentCount;
import ca.metricalsky.winston.mapper.dto.CommentDtoMapper;
import ca.metricalsky.winston.repository.AuthorRepository;
import ca.metricalsky.winston.repository.CommentRepository;
import org.apache.commons.collections4.ListUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.map.DefaultedMap.defaultedMap;

@Service
public class CommentService {

    private static final CommentCount EMPTY_COUNT = new CommentCount.Empty();

    private final CommentDtoMapper commentDtoMapper = Mappers.getMapper(CommentDtoMapper.class);

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

    public Map<String, CommentCount> getCommentCountsByChannelId(String channelId) {
        var counts = commentRepository.countCommentsForChannelIdGroupByVideoId(channelId)
                .stream()
                .collect(Collectors.toMap(CommentCount::getVideoId, count -> count));
        return defaultedMap(counts, EMPTY_COUNT);
    }

    public Map<String, CommentCount> getReplyCountsByChannelId(String channelId) {
        var counts = commentRepository.countRepliesForChannelIdGroupByVideoId(channelId)
                .stream()
                .collect(Collectors.toMap(CommentCount::getVideoId, count -> count));
        return defaultedMap(counts, EMPTY_COUNT);
    }

    public List<CommentDto> findAllByVideoId(String videoId) {
        return commentRepository.findAllByVideoIdOrderByPublishedAtAsc(videoId)
                .stream()
                .map(commentDtoMapper::fromEntity)
                .toList();
    }

    public List<CommentDto> findAllWithContextByAuthorId(String authorId) {
        return commentRepository.findAllWithContextByAuthorId(authorId)
                .stream()
                .map(commentDtoMapper::fromEntity)
                .toList();
    }
}

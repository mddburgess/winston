package ca.metricalsky.winston.service;

import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.CommentEntity;
import ca.metricalsky.winston.entity.view.CommentCountView;
import ca.metricalsky.winston.mapper.dto.CommentDtoMapper;
import ca.metricalsky.winston.repository.AuthorRepository;
import ca.metricalsky.winston.repository.CommentRepository;
import org.apache.commons.collections4.ListUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private static final CommentCountView EMPTY_COUNT = new CommentCountView.Empty();

    private final CommentDtoMapper commentDtoMapper = Mappers.getMapper(CommentDtoMapper.class);

    private final CommentRepository commentRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, AuthorRepository authorRepository) {
        this.commentRepository = commentRepository;
        this.authorRepository = authorRepository;
    }

    public Optional<CommentEntity> findById(String id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public void saveAll(List<CommentEntity> comments) {
        var authors = getAuthors(comments);
        authorRepository.saveAll(authors);
        commentRepository.saveAll(comments);
    }

    private static List<AuthorEntity> getAuthors(List<CommentEntity> comments) {
        return comments.stream()
                .map(CommentService::getCommentAndReplies)
                .flatMap(List::stream)
                .map(CommentEntity::getAuthor)
                .toList();
    }

    private static List<CommentEntity> getCommentAndReplies(CommentEntity comment) {
        return comment.getReplies() != null
                ? ListUtils.union(List.of(comment), comment.getReplies())
                : List.of(comment);
    }

    public CommentCountView getCommentCountByVideoId(String videoId) {
        return commentRepository.countCommentsForVideoId(videoId);
    }
}

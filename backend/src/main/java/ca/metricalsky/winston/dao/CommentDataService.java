package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.CommentEntity;
import ca.metricalsky.winston.mapper.entity.CommentEntityMapper;
import ca.metricalsky.winston.mappers.api.CommentMapper;
import ca.metricalsky.winston.repository.AuthorRepository;
import ca.metricalsky.winston.repository.CommentPropertiesRepository;
import ca.metricalsky.winston.repository.CommentRepository;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentDataService {

    private final CommentEntityMapper commentEntityMapper = Mappers.getMapper(CommentEntityMapper.class);

    private final AuthorRepository authorRepository;
    private final CommentMapper commentMapper;
    private final CommentPropertiesRepository commentPropertiesRepository;
    private final CommentRepository commentRepository;

    public Optional<TopLevelComment> findCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .map(commentMapper::toTopLevelComment);
    }

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

    public List<TopLevelComment> saveComments(CommentThreadListResponse commentThreadListResponse) {
        var commentEntities = commentThreadListResponse.getItems()
                .stream()
                .map(commentEntityMapper::toCommentEntity)
                .toList();

        commentEntities = saveComments(commentEntities);

        return commentEntities.stream()
                .map(commentMapper::toTopLevelComment)
                .toList();
    }

    public List<Comment> saveReplies(String parentCommentId, CommentListResponse commentListResponse) {
        var videoId = commentRepository.findById(parentCommentId)
                .map(CommentEntity::getVideoId)
                .orElse(null);
        var replyEntities = commentListResponse.getItems()
                .stream()
                .map(commentEntityMapper::toCommentEntity)
                .peek(replyEntity -> replyEntity.setVideoId(videoId))
                .toList();

        replyEntities = saveComments(replyEntities);

        return replyEntities.stream()
                .map(commentMapper::toComment)
                .toList();
    }

    public void saveCommentProperties(TopLevelComment comment) {
        var commentEntity = commentMapper.toCommentEntity(comment);
        var commentPropertiesEntity = commentEntity.getProperties();
        commentPropertiesRepository.save(commentPropertiesEntity);
    }

    private List<CommentEntity> saveComments(List<CommentEntity> commentEntities) {
        var authorEntities = getAuthors(commentEntities);
        authorRepository.saveAll(authorEntities);
        return commentRepository.saveAll(commentEntities);
    }

    private static List<AuthorEntity> getAuthors(List<CommentEntity> commentEntities) {
        return commentEntities.stream()
                .map(CommentDataService::getCommentAndReplies)
                .flatMap(List::stream)
                .map(CommentEntity::getAuthor)
                .toList();
    }

    private static List<CommentEntity> getCommentAndReplies(CommentEntity commentEntity) {
        return commentEntity.getReplies() != null
                ? ListUtils.union(List.of(commentEntity), commentEntity.getReplies())
                : List.of(commentEntity);
    }
}

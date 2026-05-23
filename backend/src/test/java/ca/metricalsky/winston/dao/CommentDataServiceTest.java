package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.database.entity.author.AuthorEntity;
import ca.metricalsky.winston.database.repository.author.AuthorJdbcRepository;
import ca.metricalsky.winston.database.entity.comment.CommentEntity;
import ca.metricalsky.winston.mappers.api.CommentMapper;
import ca.metricalsky.winston.database.repository.comment.CommentJdbcRepository;
import ca.metricalsky.winston.database.repository.comment.CommentRepository;
import ca.metricalsky.winston.test.ClientTestObjectFactory;
import ca.metricalsky.winston.test.TestUtils;
import com.google.api.services.youtube.model.CommentThread;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentDataServiceTest {

    @InjectMocks
    private CommentDataService commentDataService;

    @Mock
    private AuthorJdbcRepository authorJdbcRepository;
    @Mock
    private CommentJdbcRepository commentJdbcRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ConversionService conversionService;

    @Test
    void getCommentsForVideo_all() {
        var commentEntity = buildCommentEntity();
        var videoId = commentEntity.getVideoId();
        when(commentRepository.findCommentsForVideo(videoId))
                .thenReturn(List.of(commentEntity));

        var topLevelComment = new TopLevelComment();
        when(commentMapper.toTopLevelComment(commentEntity))
                .thenReturn(topLevelComment);

        var comments = commentDataService.getCommentsForVideo(videoId, null);

        assertThat(comments)
                .containsExactly(topLevelComment);
    }

    @Test
    void getCommentsForVideo_noComments() {
        var videoId = TestUtils.randomId();
        when(commentRepository.findCommentsForVideo(videoId))
                .thenReturn(List.of());

        var comments = commentDataService.getCommentsForVideo(videoId, null);

        assertThat(comments)
                .isEmpty();
    }

    @Test
    void getCommentsForVideo_byAuthor() {
        var commentEntity = buildCommentEntity();
        var videoId = commentEntity.getVideoId();
        var authorHandle = commentEntity.getAuthor().getDisplayName();
        when(commentRepository.findCommentsForVideoByAuthor(videoId, authorHandle))
                .thenReturn(List.of(commentEntity));

        var topLevelComment = new TopLevelComment();
        when(commentMapper.toTopLevelComment(commentEntity))
                .thenReturn(topLevelComment);

        var comments = commentDataService.getCommentsForVideo(videoId, authorHandle);

        assertThat(comments)
                .containsExactly(topLevelComment);
    }

    @Test
    void getCommentsForVideo_noCommentsByAuthor() {
        var videoId = TestUtils.randomId();
        var authorHandle = TestUtils.randomString();
        when(commentRepository.findCommentsForVideoByAuthor(videoId, authorHandle))
                .thenReturn(List.of());

        var comments = commentDataService.getCommentsForVideo(videoId, authorHandle);

        assertThat(comments)
                .isEmpty();
    }

    @Test
    void saveComments() {
        var commentThreadListResponse = ClientTestObjectFactory.buildCommentThreadListResponse();
        var topLevelComment = new TopLevelComment();

        when(conversionService.convert(any(CommentThread.class), eq(CommentEntity.class)))
                .thenReturn(new CommentEntity());
        when(commentJdbcRepository.saveAll(anyList()))
                .thenAnswer(returnsFirstArg());
        when(commentMapper.toTopLevelComment(any(CommentEntity.class)))
                .thenReturn(topLevelComment);

        var comments = commentDataService.saveComments(commentThreadListResponse);

        assertThat(comments)
                .containsExactly(topLevelComment);
    }

    @Test
    void saveReplies() {
        var parentCommentId = TestUtils.randomId();
        var commentListResponse = ClientTestObjectFactory.buildCommentListResponse();
        var commentEntity = buildCommentEntity();
        var comment = new Comment();

        when(commentRepository.findById(parentCommentId))
                .thenReturn(Optional.of(commentEntity));
        when(conversionService.convert(
                any(com.google.api.services.youtube.model.Comment.class), eq(CommentEntity.class)))
                .thenReturn(new CommentEntity());
        when(commentJdbcRepository.saveAll(anyList()))
                .thenAnswer(returnsFirstArg());
        when(commentMapper.toComment(any(CommentEntity.class)))
                .thenReturn(comment);

        var comments = commentDataService.saveReplies(parentCommentId, commentListResponse);

        assertThat(comments)
                .containsExactly(comment);
    }

    private static CommentEntity buildCommentEntity() {
        var authorEntity = new AuthorEntity(
                TestUtils.randomId(),
                TestUtils.randomString(),
                TestUtils.randomString(),
                TestUtils.randomString(),
                Set.of()
        );
        var commentEntity = new CommentEntity();
        commentEntity.setId(TestUtils.randomId());
        commentEntity.setVideoId(TestUtils.randomId());
        commentEntity.setAuthor(authorEntity);
        return commentEntity;
    }
}

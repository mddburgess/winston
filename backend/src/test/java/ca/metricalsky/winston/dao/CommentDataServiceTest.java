package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.CommentEntity;
import ca.metricalsky.winston.mappers.api.CommentMapper;
import ca.metricalsky.winston.repository.AuthorRepository;
import ca.metricalsky.winston.repository.CommentRepository;
import ca.metricalsky.winston.test.ClientTestObjectFactory;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentDataServiceTest {

    @InjectMocks
    private CommentDataService commentDataService;

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentRepository commentRepository;

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

        when(commentRepository.saveAll(anyList()))
                .thenAnswer(returnsFirstArg());

        var topLevelComment = new TopLevelComment();
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
        when(commentRepository.findById(parentCommentId))
                .thenReturn(Optional.of(commentEntity));

        when(commentRepository.saveAll(anyList()))
                .thenAnswer(returnsFirstArg());

        var comment = new Comment();
        when(commentMapper.toComment(any(CommentEntity.class)))
                .thenReturn(comment);

        var comments = commentDataService.saveReplies(parentCommentId, commentListResponse);

        assertThat(comments)
                .containsExactly(comment);
    }

    private static CommentEntity buildCommentEntity() {
        var authorEntity = AuthorEntity.builder()
                .id(TestUtils.randomId())
                .displayName(TestUtils.randomString())
                .build();
        return CommentEntity.builder()
                .id(TestUtils.randomId())
                .videoId(TestUtils.randomId())
                .author(authorEntity)
                .build();
    }
}

package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.CommentEntity;
import ca.metricalsky.winston.test.RepositoryTest;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class CommentJdbcRepositoryTest {

    @Autowired
    private CommentJdbcRepository commentJdbcRepository;
    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void afterEach() {
        commentRepository.deleteAll();
    }

    @Test
    void saveAll() {
        var commentsToInsert = new ArrayList<CommentEntity>();
        for (int i = 0; i < 5; ++i) {
            commentsToInsert.add(createComment());
        }

        var result = commentJdbcRepository.saveAll(commentsToInsert);

        assertThat(result).isEqualTo(commentsToInsert);
        assertThat(commentRepository.findAll()).hasSize(10);

        var commentsToInsertOrUpdate = new ArrayList<CommentEntity>();
        for (var comment : commentsToInsert) {
            commentsToInsertOrUpdate.add(copyComment(comment));
        }
        for (int i = 0; i < 5; ++i) {
            commentsToInsertOrUpdate.add(createComment());
        }

        result = commentJdbcRepository.saveAll(commentsToInsertOrUpdate);

        assertThat(result).isEqualTo(commentsToInsertOrUpdate);
        assertThat(commentRepository.findAll()).hasSize(20);
    }

    @Test
    void saveAllEmptyList() {
        var result = commentJdbcRepository.saveAll(List.of());

        assertThat(result).isEmpty();
        assertThat(commentRepository.findAll()).isEmpty();
    }

    private static CommentEntity createComment() {
        var id = TestUtils.randomId();
        var author = AuthorEntity.builder()
                .id(TestUtils.randomId())
                .build();
        return CommentEntity.builder()
                .id(id)
                .videoId(TestUtils.randomId())
                .author(author)
                .textDisplay(TestUtils.randomString())
                .textOriginal(TestUtils.randomString())
                .totalReplyCount(TestUtils.randomLong())
                .replies(List.of(createReply(id)))
                .publishedAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .likeCount(TestUtils.randomLong())
                .build();
    }

    private static CommentEntity createReply(String parentId) {
        var author = AuthorEntity.builder()
                .id(TestUtils.randomId())
                .build();
        return CommentEntity.builder()
                .id(TestUtils.randomId())
                .videoId(TestUtils.randomId())
                .parentId(parentId)
                .author(author)
                .textDisplay(TestUtils.randomString())
                .textOriginal(TestUtils.randomString())
                .totalReplyCount(TestUtils.randomLong())
                .publishedAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .likeCount(TestUtils.randomLong())
                .build();
    }

    private static CommentEntity copyComment(CommentEntity commentEntity) {
        return CommentEntity.builder()
                .id(commentEntity.getId())
                .videoId(commentEntity.getVideoId())
                .parentId(commentEntity.getParentId())
                .author(commentEntity.getAuthor())
                .textDisplay(commentEntity.getTextDisplay())
                .textOriginal(commentEntity.getTextOriginal())
                .totalReplyCount(commentEntity.getTotalReplyCount())
                .replies(commentEntity.getReplies())
                .publishedAt(commentEntity.getPublishedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .likeCount(commentEntity.getLikeCount())
                .build();
    }
}

//package ca.metricalsky.winston.repository;
//
//import ca.metricalsky.winston.database.entity.author.AuthorEntity;
//import ca.metricalsky.winston.database.entity.comment.CommentEntity;
//import ca.metricalsky.winston.database.repository.comment.CommentJdbcRepository;
//import ca.metricalsky.winston.database.repository.comment.CommentRepository;
//import ca.metricalsky.winston.test.annotations.RepositoryTest;
//import ca.metricalsky.winston.test.TestUtils;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.OffsetDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RepositoryTest
//class CommentJdbcRepositoryTest {
//
//    @Autowired
//    private CommentJdbcRepository commentJdbcRepository;
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @AfterEach
//    void afterEach() {
//        commentRepository.deleteAll();
//    }
//
//    @Test
//    void saveAll() {
//        var commentsToInsert = new ArrayList<CommentEntity>();
//        for (int i = 0; i < 5; ++i) {
//            commentsToInsert.add(createComment());
//        }
//
//        var result = commentJdbcRepository.saveAll(commentsToInsert);
//
//        assertThat(result).isEqualTo(commentsToInsert);
//        assertThat(commentRepository.findAll()).hasSize(10);
//
//        var commentsToInsertOrUpdate = new ArrayList<CommentEntity>();
//        for (var comment : commentsToInsert) {
//            commentsToInsertOrUpdate.add(copyComment(comment));
//        }
//        for (int i = 0; i < 5; ++i) {
//            commentsToInsertOrUpdate.add(createComment());
//        }
//
//        result = commentJdbcRepository.saveAll(commentsToInsertOrUpdate);
//
//        assertThat(result).isEqualTo(commentsToInsertOrUpdate);
//        assertThat(commentRepository.findAll()).hasSize(20);
//    }
//
//    @Test
//    void saveAllEmptyList() {
//        var result = commentJdbcRepository.saveAll(List.of());
//
//        assertThat(result).isEmpty();
//        assertThat(commentRepository.findAll()).isEmpty();
//    }
//
//    private static CommentEntity createComment() {
//        var id = TestUtils.randomId();
//        var author = new AuthorEntity(
//                id,
//                TestUtils.randomString(),
//                TestUtils.randomString(),
//                TestUtils.randomString(),
//                OffsetDateTime.now(),
//                Set.of());
//
//        var commentEntity = new CommentEntity();
//        commentEntity.setId(id);
//        commentEntity.setVideoId(TestUtils.randomId());
//        commentEntity.setAuthor(author);
//        commentEntity.setTextDisplay(TestUtils.randomString());
//        commentEntity.setTextOriginal(TestUtils.randomString());
//        commentEntity.setTotalReplyCount(TestUtils.randomLong());
//        commentEntity.setReplies(List.of(createReply(id)));
//        commentEntity.setPublishedAt(OffsetDateTime.now());
//        commentEntity.setUpdatedAt(OffsetDateTime.now());
//        commentEntity.setLikeCount(TestUtils.randomLong());
//        return commentEntity;
//    }
//
//    private static CommentEntity createReply(String parentId) {
//        var author = new AuthorEntity(
//                TestUtils.randomId(),
//                TestUtils.randomString(),
//                TestUtils.randomString(),
//                TestUtils.randomString(),
//                OffsetDateTime.now(),
//                Set.of());
//
//        var commentEntity = new CommentEntity();
//        commentEntity.setId(TestUtils.randomId());
//        commentEntity.setVideoId(TestUtils.randomId());
//        commentEntity.setParentId(parentId);
//        commentEntity.setAuthor(author);
//        commentEntity.setTextDisplay(TestUtils.randomString());
//        commentEntity.setTextOriginal(TestUtils.randomString());
//        commentEntity.setTotalReplyCount(TestUtils.randomLong());
//        commentEntity.setPublishedAt(OffsetDateTime.now());
//        commentEntity.setUpdatedAt(OffsetDateTime.now());
//        commentEntity.setLikeCount(TestUtils.randomLong());
//        return commentEntity;
//    }
//
//    private static CommentEntity copyComment(CommentEntity commentEntity) {
//        var copy = new CommentEntity();
//        copy.setId(commentEntity.getId());
//        copy.setVideoId(commentEntity.getVideoId());
//        copy.setParentId(commentEntity.getParentId());
//        copy.setAuthor(commentEntity.getAuthor());
//        copy.setTextDisplay(commentEntity.getTextDisplay());
//        copy.setTextOriginal(commentEntity.getTextOriginal());
//        copy.setTotalReplyCount(commentEntity.getTotalReplyCount());
//        copy.setReplies(commentEntity.getReplies());
//        copy.setPublishedAt(commentEntity.getPublishedAt());
//        copy.setUpdatedAt(commentEntity.getUpdatedAt());
//        copy.setLikeCount(commentEntity.getLikeCount());
//        return copy;
//    }
//}

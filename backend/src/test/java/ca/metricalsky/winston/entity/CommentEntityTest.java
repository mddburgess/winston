package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CommentEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private VideoEntity videoEntity;

    @BeforeEach
    void beforeEach() {
        var channelEntity = persistChannel();
        videoEntity = persistVideo(channelEntity);
    }

    @Test
    void persistsWithOnlyRequiredFields() {
        var commentEntity = CommentEntity.builder()
                .id(TestUtils.randomId())
                .videoId(videoEntity.getId())
                .build();

        var persistedEntity = entityManager.persistFlushFind(commentEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", commentEntity.getId())
                .hasFieldOrPropertyWithValue("videoId", commentEntity.getVideoId())
                .hasAllNullFieldsOrPropertiesExcept("id", "lastFetchedAt", "replies", "videoId");
        assertThat(persistedEntity.getReplies())
                .isEmpty();
    }

    @Test
    void persistsWithAllOptionalFields() {
        var commentEntity = CommentEntity.builder()
                .id(TestUtils.randomId())
                .videoId(videoEntity.getId())
                .textDisplay(TestUtils.randomString())
                .textOriginal(TestUtils.randomString())
                .totalReplyCount(TestUtils.randomLong())
                .publishedAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        var persistedEntity = entityManager.persistFlushFind(commentEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", commentEntity.getId())
                .hasFieldOrPropertyWithValue("videoId", commentEntity.getVideoId())
                .hasFieldOrPropertyWithValue("textDisplay", commentEntity.getTextDisplay())
                .hasFieldOrPropertyWithValue("textOriginal", commentEntity.getTextOriginal())
                .hasFieldOrPropertyWithValue("totalReplyCount", commentEntity.getTotalReplyCount())
                .hasFieldOrPropertyWithValue("publishedAt",
                        commentEntity.getPublishedAt().withOffsetSameInstant(ZoneOffset.UTC))
                .hasFieldOrPropertyWithValue("updatedAt",
                        commentEntity.getUpdatedAt().withOffsetSameInstant(ZoneOffset.UTC))
                .hasNoNullFieldsOrPropertiesExcept("author", "parentId");
        assertThat(persistedEntity.getReplies())
                .isEmpty();
    }

    @Test
    void persistsReplies() {
        var commentEntity = CommentEntity.builder()
                .id(TestUtils.randomId())
                .videoId(videoEntity.getId())
                .build();
        var replyEntity = CommentEntity.builder()
                .id(TestUtils.randomId())
                .videoId(videoEntity.getId())
                .build();
        commentEntity.setReplies(List.of(replyEntity));

        var persistedEntity = entityManager.persistFlushFind(commentEntity);

        assertThat(persistedEntity.getReplies())
                .hasSize(1);

        var persistedReplyEntity = persistedEntity.getReplies().getFirst();
        persistedReplyEntity = entityManager.refresh(persistedReplyEntity);

        assertThat(persistedReplyEntity)
                .hasFieldOrPropertyWithValue("id", replyEntity.getId())
                .hasFieldOrPropertyWithValue("videoId", replyEntity.getVideoId())
                .hasFieldOrPropertyWithValue("parentId", commentEntity.getId());
    }

    private ChannelEntity persistChannel() {
        var channelEntity = ChannelEntity.builder()
                .id(TestUtils.randomId())
                .build();
        return entityManager.persist(channelEntity);
    }

    private VideoEntity persistVideo(ChannelEntity channelEntity) {
        var videoEntity = VideoEntity.builder()
                .id(TestUtils.randomId())
                .channelId(channelEntity.getId())
                .build();
        return entityManager.persist(videoEntity);
    }
}

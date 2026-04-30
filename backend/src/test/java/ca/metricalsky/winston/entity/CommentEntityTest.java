package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.database.entity.channel.ChannelEntity;
import ca.metricalsky.winston.database.entity.comment.CommentEntity;
import ca.metricalsky.winston.database.entity.video.VideoEntity;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;
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
        var commentEntity = new CommentEntity();
        commentEntity.setId(TestUtils.randomId());
        commentEntity.setVideoId(videoEntity.getId());

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
        var commentEntity = new CommentEntity();
        commentEntity.setId(TestUtils.randomId());
        commentEntity.setVideoId(videoEntity.getId());
        commentEntity.setTextDisplay(TestUtils.randomString());
        commentEntity.setTextOriginal(TestUtils.randomString());
        commentEntity.setLikeCount(TestUtils.randomLong());
        commentEntity.setTotalReplyCount(TestUtils.randomLong());
        commentEntity.setPublishedAt(OffsetDateTime.now());
        commentEntity.setUpdatedAt(OffsetDateTime.now());

        var persistedEntity = entityManager.persistFlushFind(commentEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", commentEntity.getId())
                .hasFieldOrPropertyWithValue("videoId", commentEntity.getVideoId())
                .hasFieldOrPropertyWithValue("textDisplay", commentEntity.getTextDisplay())
                .hasFieldOrPropertyWithValue("textOriginal", commentEntity.getTextOriginal())
                .hasFieldOrPropertyWithValue("likeCount", commentEntity.getLikeCount())
                .hasFieldOrPropertyWithValue("totalReplyCount", commentEntity.getTotalReplyCount())
                .hasNoNullFieldsOrPropertiesExcept("author", "parentId", "properties");
        assertThat(persistedEntity.getReplies())
                .isEmpty();
    }

    @Test
    void persistsReplies() {
        var commentEntity = new CommentEntity();
        commentEntity.setId(TestUtils.randomId());
        commentEntity.setVideoId(videoEntity.getId());
        var replyEntity = new CommentEntity();
        replyEntity.setId(TestUtils.randomId());
        replyEntity.setVideoId(videoEntity.getId());
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
        var channelEntity = new ChannelEntity();
        channelEntity.setId(TestUtils.randomId());
        return entityManager.persist(channelEntity);
    }

    private VideoEntity persistVideo(ChannelEntity channelEntity) {
        var videoEntity = new VideoEntity();
        videoEntity.setId(TestUtils.randomId());
        videoEntity.setChannelId(channelEntity.getId());
        return entityManager.persist(videoEntity);
    }
}

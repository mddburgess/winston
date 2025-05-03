package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.Channel;
import ca.metricalsky.winston.entity.Comment;
import ca.metricalsky.winston.entity.Video;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = CommentRepository.class
))
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CommentRepository repository;

    @Test
    void countCommentsByChannelCustomUrl() {
        var channel = entityManager.persist(buildChannel());
        var video = entityManager.persist(buildVideo(channel));
        var comment = entityManager.persist(buildComment(video));
        entityManager.persist(buildReply(comment));

        var commentCounts = repository.countCommentsByChannelCustomUrl(channel.getCustomUrl());

        assertThat(commentCounts)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("videoId", video.getId())
                .hasFieldOrPropertyWithValue("comments", 1L)
                .hasFieldOrPropertyWithValue("commentsAndReplies", 2L)
                .hasFieldOrPropertyWithValue("replies", 1L)
                .hasFieldOrPropertyWithValue("totalReplies", 3L);
    }

    @Test
    void countCommentsByChannelCustomUrl_noComments() {
        var channel = entityManager.persist(buildChannel());
        entityManager.persist(buildVideo(channel));

        var commentCounts = repository.countCommentsByChannelCustomUrl(channel.getCustomUrl());

        assertThat(commentCounts)
                .isEmpty();
    }

    @Test
    void countCommentsByChannelCustomUrl_noVideos() {
        var channel = entityManager.persist(buildChannel());

        var commentCounts = repository.countCommentsByChannelCustomUrl(channel.getCustomUrl());

        assertThat(commentCounts)
                .isEmpty();
    }

    @Test
    void countCommentsByChannelCustomUrl_noChannel() {
        var channelHandle = RandomStringUtils.secure().nextAlphanumeric(10);

        var commentCounts = repository.countCommentsByChannelCustomUrl(channelHandle);

        assertThat(commentCounts)
                .isEmpty();
    }

    private static Channel buildChannel() {
        var channel = new Channel();
        channel.setId(RandomStringUtils.secure().nextAlphanumeric(10));
        channel.setCustomUrl("@" + RandomStringUtils.secure().nextAlphanumeric(10));
        return channel;
    }

    private static Video buildVideo(Channel channel) {
        var video = new Video();
        video.setId(RandomStringUtils.secure().nextAlphanumeric(10));
        video.setChannelId(channel.getId());
        return video;
    }

    private static Comment buildComment(Video video) {
        var comment = new Comment();
        comment.setId(RandomStringUtils.secure().nextAlphanumeric(10));
        comment.setVideoId(video.getId());
        comment.setTotalReplyCount(3L);
        return comment;
    }

    private static Comment buildReply(Comment comment) {
        var reply = new Comment();
        reply.setId(RandomStringUtils.secure().nextAlphanumeric(10));
        reply.setVideoId(comment.getVideoId());
        reply.setParentId(comment.getId());
        return reply;
    }
}

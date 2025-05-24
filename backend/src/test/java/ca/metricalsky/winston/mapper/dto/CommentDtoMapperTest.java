package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.CommentEntity;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentDtoMapperTest {

    private final CommentDtoMapper commentDtoMapper = new CommentDtoMapperImpl();

    @Test
    void fromEntity() {
        var comment = buildComment();

        var commentDto = commentDtoMapper.fromEntity(comment);

        assertThat(commentDto)
                .hasFieldOrPropertyWithValue("id", comment.getId())
                .hasFieldOrPropertyWithValue("videoId", comment.getVideoId())
                .hasFieldOrPropertyWithValue("author.id", comment.getAuthor().getId())
                .hasFieldOrPropertyWithValue("author.displayName", comment.getAuthor().getDisplayName())
                .hasFieldOrPropertyWithValue("author.channelUrl", comment.getAuthor().getChannelUrl())
                .hasFieldOrPropertyWithValue("author.profileImageUrl",
                        "/api/v1/authors/" + comment.getAuthor().getId() + "/thumbnail")
                .hasFieldOrPropertyWithValue("text", comment.getTextDisplay())
                .hasFieldOrPropertyWithValue("publishedAt", comment.getPublishedAt())
                .hasFieldOrPropertyWithValue("updatedAt", comment.getUpdatedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", comment.getLastFetchedAt())
                .hasFieldOrPropertyWithValue("replies", List.of());
    }

    @Test
    void fromEntity_nullComment() {
        var commentDto = commentDtoMapper.fromEntity(null);
        assertThat(commentDto).isNull();
    }

    @Test
    void fromEntity_emptyComment() {
        var commentDto = commentDtoMapper.fromEntity(new CommentEntity());
        assertThat(commentDto)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    private static CommentEntity buildComment() {
        var comment = new CommentEntity();
        comment.setId("id");
        comment.setVideoId("videoId");
        comment.setAuthor(buildAuthor());
        comment.setTextDisplay("textDisplay");
        comment.setTextOriginal("textOriginal");
        comment.setTotalReplyCount(1L);
        comment.setPublishedAt(OffsetDateTime.now());
        comment.setUpdatedAt(OffsetDateTime.now());
        comment.setLastFetchedAt(OffsetDateTime.now());
        return comment;
    }

    private static AuthorEntity buildAuthor() {
        var author = new AuthorEntity();
        author.setId("id");
        author.setDisplayName("displayName");
        author.setChannelUrl("channelUrl");
        author.setProfileImageUrl("profileImageUrl");
        author.setLastFetchedAt(OffsetDateTime.now());
        return author;
    }
}

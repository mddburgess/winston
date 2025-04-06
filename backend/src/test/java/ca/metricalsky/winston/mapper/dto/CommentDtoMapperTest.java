package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.entity.Author;
import ca.metricalsky.winston.entity.Comment;
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
                .hasFieldOrPropertyWithValue("author.profileImageUrl", comment.getAuthor().getProfileImageUrl())
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
        var commentDto = commentDtoMapper.fromEntity(new Comment());
        assertThat(commentDto)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    private static Comment buildComment() {
        var comment = new Comment();
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

    private static Author buildAuthor() {
        var author = new Author();
        author.setId("id");
        author.setDisplayName("displayName");
        author.setChannelUrl("channelUrl");
        author.setProfileImageUrl("profileImageUrl");
        author.setLastFetchedAt(OffsetDateTime.now());
        return author;
    }
}

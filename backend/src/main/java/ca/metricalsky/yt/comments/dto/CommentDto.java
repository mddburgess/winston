package ca.metricalsky.yt.comments.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class CommentDto {

    private String id;

    private AuthorDto author;

    private String text;

    private OffsetDateTime publishedAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime lastFetchedAt;

    private List<CommentDto> replies;
}

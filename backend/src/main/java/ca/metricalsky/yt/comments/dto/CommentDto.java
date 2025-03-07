package ca.metricalsky.yt.comments.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class CommentDto {

    private String id;

    private AuthorDto author;

    private String text;

    private OffsetDateTime publishedAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime lastFetchedAt;

    private List<CommentDto> replies;
}

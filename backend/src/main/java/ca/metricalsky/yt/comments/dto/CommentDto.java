package ca.metricalsky.yt.comments.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    private String id;

    private AuthorDto author;

    private String text;

    private String publishedAt;

    private String updatedAt;

    private String lastFetchedAt;
}

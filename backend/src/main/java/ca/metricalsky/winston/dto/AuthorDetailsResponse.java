package ca.metricalsky.winston.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthorDetailsResponse {

    private AuthorDto author;
    private List<CommentDto> comments;
    private List<VideoDto> videos;
}

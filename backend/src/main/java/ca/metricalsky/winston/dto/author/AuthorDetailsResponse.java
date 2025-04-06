package ca.metricalsky.winston.dto.author;

import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.dto.VideoDto;
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

package ca.metricalsky.winston.dto.author;

import lombok.Data;

@Deprecated(since = "1.4.0", forRemoval = true)
@Data
public class AuthorStatistics {

    private Long commentedVideos;

    private Long totalComments;

    private Long totalReplies;
}

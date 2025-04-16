package ca.metricalsky.winston.dto.author;

import lombok.Data;

@Data
public class AuthorStatistics {

    private Long commentedVideos;

    private Long totalComments;

    private Long totalReplies;
}

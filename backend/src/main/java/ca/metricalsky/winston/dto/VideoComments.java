package ca.metricalsky.winston.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class VideoComments {

    private boolean commentsDisabled;

    private Long commentCount;

    private Long replyCount;

    private Long totalReplyCount;

    private OffsetDateTime lastFetchedAt;
}

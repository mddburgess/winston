package ca.metricalsky.yt.comments.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class VideoDto {

    private String id;

    private String channelId;

    private String title;

    private String description;

    private String thumbnailUrl;

    private Long commentCount;

    private Long replyCount;

    private Long totalReplyCount;

    private OffsetDateTime publishedAt;

    private OffsetDateTime lastFetchedAt;
}

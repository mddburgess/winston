package ca.metricalsky.yt.comments.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class VideoDto {

    private String id;

    private String channelId;

    private ChannelDto channel;

    private String title;

    private String description;

    private String thumbnailUrl;

    private Long commentCount;

    private Long replyCount;

    private Long totalReplyCount;

    private OffsetDateTime publishedAt;

    private OffsetDateTime lastFetchedAt;
}

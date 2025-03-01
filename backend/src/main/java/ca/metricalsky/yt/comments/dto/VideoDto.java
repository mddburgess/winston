package ca.metricalsky.yt.comments.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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

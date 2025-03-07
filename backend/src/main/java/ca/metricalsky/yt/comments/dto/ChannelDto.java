package ca.metricalsky.yt.comments.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.TreeSet;


@Data
public class ChannelDto {

    private String id;

    private String title;

    private String description;

    private String customUrl;

    private String thumbnailUrl;

    private TreeSet<String> topics;

    private TreeSet<String> keywords;

    private Long videoCount;

    private OffsetDateTime publishedAt;

    private OffsetDateTime lastFetchedAt;
}

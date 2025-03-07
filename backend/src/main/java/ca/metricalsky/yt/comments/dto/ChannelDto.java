package ca.metricalsky.yt.comments.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.TreeSet;


@Data
public class ChannelDto {

    @NonNull
    private String id = "";

    @NonNull
    private String title = "";

    @NonNull
    private String description = "";

    @NonNull
    private String customUrl = "";

    @NonNull
    private String thumbnailUrl = "";

    @NonNull
    private TreeSet<String> topics = new TreeSet<>();

    @NonNull
    private TreeSet<String> keywords = new TreeSet<>();

    @NonNull
    private Long videoCount = 0L;

    @NonNull
    private OffsetDateTime publishedAt = Instant.EPOCH.atOffset(ZoneOffset.UTC);

    @NonNull
    private OffsetDateTime lastFetchedAt = Instant.EPOCH.atOffset(ZoneOffset.UTC);
}

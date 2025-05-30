package ca.metricalsky.winston.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Data
public class VideoDto {

    @NonNull
    private String id = "";

    private String channelId;

    private ChannelDto channel;

    @NonNull
    private String title = "";

    @NonNull
    private String description = "";

    @NonNull
    private String thumbnailUrl = "";

    private VideoComments comments;

    @NonNull
    private OffsetDateTime publishedAt = Instant.EPOCH.atOffset(ZoneOffset.UTC);

    @NonNull
    private OffsetDateTime lastFetchedAt = Instant.EPOCH.atOffset(ZoneOffset.UTC);
}

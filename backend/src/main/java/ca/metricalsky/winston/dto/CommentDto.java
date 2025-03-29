package ca.metricalsky.winston.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentDto {

    @NonNull
    private String id = "";

    @NonNull
    private AuthorDto author = new AuthorDto();

    @NonNull
    private String text = "";

    @NonNull
    private OffsetDateTime publishedAt = Instant.EPOCH.atOffset(ZoneOffset.UTC);

    @NonNull
    private OffsetDateTime updatedAt = Instant.EPOCH.atOffset(ZoneOffset.UTC);

    @NonNull
    private OffsetDateTime lastFetchedAt = Instant.EPOCH.atOffset(ZoneOffset.UTC);

    @NonNull
    private Long totalReplyCount = 0L;

    @NonNull
    private List<CommentDto> replies = new ArrayList<>();
}

package ca.metricalsky.winston.dto.author;

import lombok.Data;
import org.springframework.lang.NonNull;

@Deprecated(since = "1.4.0", forRemoval = true)
@Data
public class AuthorDto {

    @NonNull
    private String id = "";

    @NonNull
    private String displayName = "";

    @NonNull
    private String channelUrl = "";

    @NonNull
    private String profileImageUrl = "";

    private AuthorStatistics statistics;
}

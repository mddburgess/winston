package ca.metricalsky.winston.dto.author;

import lombok.Data;
import org.springframework.lang.NonNull;

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

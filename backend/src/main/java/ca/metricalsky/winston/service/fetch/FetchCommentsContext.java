package ca.metricalsky.winston.service.fetch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchCommentsContext {

    private final String videoId;
    private String pageToken;

    public boolean hasNext() {
        return pageToken != null;
    }
}

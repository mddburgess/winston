package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchOperation;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class FetchVideosContext {

    private final String channelId;
    private final OffsetDateTime lastPublishedAt;
    private String nextPageToken;
    private FetchRequest fetchRequest;
    private FetchOperation currentOperation;

    public boolean hasNext() {
        return nextPageToken != null;
    }
}

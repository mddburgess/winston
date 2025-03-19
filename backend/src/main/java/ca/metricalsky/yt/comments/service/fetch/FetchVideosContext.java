package ca.metricalsky.yt.comments.service.fetch;

import ca.metricalsky.yt.comments.entity.fetch.FetchRequest;
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

    public boolean hasNext() {
        return nextPageToken != null;
    }
}

package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class FetchContext {

    private final String objectId;
    private final OffsetDateTime publishedAfter;
    private OffsetDateTime publishedBefore;
    private String pageToken;
    private FetchRequest fetchRequest;
}

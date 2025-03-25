package ca.metricalsky.winston.dto.fetch;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class FetchRange {

    private OffsetDateTime after;
    private OffsetDateTime before;
}

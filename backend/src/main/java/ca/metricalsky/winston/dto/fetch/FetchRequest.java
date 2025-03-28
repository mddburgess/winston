package ca.metricalsky.winston.dto.fetch;

import lombok.Data;

@Data
public class FetchRequest {

    private FetchChannel channel;
    private FetchVideos videos;
    private FetchComments comments;
}

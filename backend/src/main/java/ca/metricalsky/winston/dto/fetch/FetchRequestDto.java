package ca.metricalsky.winston.dto.fetch;

import lombok.Data;

@Data
public class FetchRequestDto {

    private FetchChannel channel;
    private FetchVideos videos;
    private FetchComments comments;
    private FetchReplies replies;
}

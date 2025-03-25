package ca.metricalsky.winston.dto.fetch;

import lombok.Data;

@Data
public class FetchVideos {

    private String channelId;
    private Mode fetch;
    private FetchRange range;

    public enum Mode {
        ALL,
        LATEST,
        RANGE,
    }
}

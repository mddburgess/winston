package ca.metricalsky.winston.dto;

import ca.metricalsky.winston.entity.VideoEntity;

import java.time.OffsetDateTime;
import java.util.List;

public record FetchVideosResponse(
    int count,
    int totalCount,
    String nextPageToken,
    OffsetDateTime nextPublishedBefore,
    List<VideoEntity> videos
) {

}

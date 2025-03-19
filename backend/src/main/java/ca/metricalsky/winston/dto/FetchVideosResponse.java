package ca.metricalsky.winston.dto;

import ca.metricalsky.winston.entity.Video;

import java.util.List;

public record FetchVideosResponse(
    int count,
    int totalCount,
    String nextPageToken,
    List<Video> videos
) {

}

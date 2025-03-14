package ca.metricalsky.yt.comments.dto;

import ca.metricalsky.yt.comments.entity.Video;

import java.util.List;

public record FetchVideosResponse(
    int count,
    int totalCount,
    String nextPageToken,
    List<Video> videos
) {

}

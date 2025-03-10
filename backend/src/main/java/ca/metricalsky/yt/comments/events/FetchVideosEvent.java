package ca.metricalsky.yt.comments.events;

import ca.metricalsky.yt.comments.dto.VideoDto;

import java.util.List;

public record FetchVideosEvent(String channelId, FetchStatus status, List<VideoDto> videos) {

}

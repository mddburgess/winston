package ca.metricalsky.winston.events;

import ca.metricalsky.winston.dto.VideoDto;

import java.util.List;

public record FetchVideosEvent(String channelId, FetchStatus status, List<VideoDto> videos) {

}

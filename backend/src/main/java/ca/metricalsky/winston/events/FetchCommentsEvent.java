package ca.metricalsky.winston.events;

import ca.metricalsky.winston.dto.CommentDto;

import java.util.List;

public record FetchCommentsEvent(String videoId, FetchStatus status, List<CommentDto> comments) {

}

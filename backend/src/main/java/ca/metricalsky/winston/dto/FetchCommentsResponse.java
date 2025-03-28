package ca.metricalsky.winston.dto;

import ca.metricalsky.winston.entity.Comment;

import java.util.List;

public record FetchCommentsResponse(List<Comment> comments, String nextPageToken) {
}

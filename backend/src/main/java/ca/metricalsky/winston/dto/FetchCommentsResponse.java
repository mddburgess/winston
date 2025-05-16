package ca.metricalsky.winston.dto;

import ca.metricalsky.winston.entity.CommentEntity;

import java.util.List;

public record FetchCommentsResponse(List<CommentEntity> comments, String nextPageToken) {
}

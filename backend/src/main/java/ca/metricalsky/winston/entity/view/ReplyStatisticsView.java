package ca.metricalsky.winston.entity.view;

import java.time.OffsetDateTime;

public interface ReplyStatisticsView {

    String getCommentId();

    OffsetDateTime getCommentPublishedAt();

    Integer getCommentReplyCount();

    OffsetDateTime getCommentLastFetchedAt();

    Integer getFetchedReplyCount();

    OffsetDateTime getMostRecentReplyPublishedAt();
}

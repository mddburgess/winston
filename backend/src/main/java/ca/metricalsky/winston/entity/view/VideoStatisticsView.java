package ca.metricalsky.winston.entity.view;

import java.time.OffsetDateTime;

public interface VideoStatisticsView {

    String getChannelId();

    String getVideoId();

    Long getCommentCount();

    Long getReplyCount();

    OffsetDateTime getLastCommentedAt();
}

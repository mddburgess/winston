package ca.metricalsky.winston.entity.view;

import java.time.OffsetDateTime;

public interface AuthorVideoView {

    String getVideoId();

    String getVideoTitle();

    String getVideoThumbnailUrl();

    Long getTotalCommentCount();

    Long getReplyCount();

    OffsetDateTime getFirstCommentedAt();

    OffsetDateTime getLastCommentedAt();

    default Long getCommentCount() {
        return getTotalCommentCount() - getReplyCount();
    }
}

package ca.metricalsky.winston.entity.view;

import java.time.OffsetDateTime;

public interface AuthorChannelView {

    String getChannelTitle();

    String getChannelHandle();

    Long getVideoCount();

    Long getTotalCommentCount();

    Long getReplyCount();

    OffsetDateTime getFirstCommentedAt();

    OffsetDateTime getLastCommentedAt();

    default Long getCommentCount() {
        return getTotalCommentCount() - getReplyCount();
    }
}

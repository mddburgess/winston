package ca.metricalsky.winston.entity.view;

import java.time.OffsetDateTime;


public interface ChannelVideoStatisticsView {

    String getChannelHandle();

    String getChannelId();

    OffsetDateTime getChannelPublishedAt();

    Integer getChannelVideoCount();

    Integer getVideoCount();

    OffsetDateTime getLatestVideoPublishedAt();
}

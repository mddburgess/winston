package ca.metricalsky.winston.entity.view;

import ca.metricalsky.winston.database.entity.channel.ChannelEntity;
import ca.metricalsky.winston.entity.VideoEntity;

public interface ChannelVideoView {

    ChannelEntity getChannel();

    VideoEntity getVideo();
}

package ca.metricalsky.winston.entity.view;

import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.entity.VideoEntity;

public interface ChannelVideoView {

    ChannelEntity getChannel();

    VideoEntity getVideo();
}

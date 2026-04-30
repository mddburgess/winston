package ca.metricalsky.winston.database.view

import ca.metricalsky.winston.database.entity.channel.ChannelEntity
import ca.metricalsky.winston.database.entity.video.VideoEntity

interface ChannelVideoView {

    val channel: ChannelEntity
    val video: VideoEntity
}

package ca.metricalsky.winston.events;

import ca.metricalsky.winston.dto.ChannelDto;

public record FetchChannelEvent(String channelHandle, FetchStatus status, ChannelDto channel) {

}

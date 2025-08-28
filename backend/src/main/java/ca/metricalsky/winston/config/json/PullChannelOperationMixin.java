package ca.metricalsky.winston.config.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "pull",
        "channel_handle",
        "status",
        "id",
})
interface PullChannelOperationMixin {

}

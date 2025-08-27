package ca.metricalsky.winston.config.json;

import ca.metricalsky.winston.api.model.PullChannelOperation;
import ca.metricalsky.winston.api.model.PullCommentsOperation;
import ca.metricalsky.winston.api.model.PullRepliesOperation;
import ca.metricalsky.winston.api.model.PullVideosOperation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(
        value = "pull",
        allowSetters = true
)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "pull",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PullChannelOperation.class, name = "channel"),
        @JsonSubTypes.Type(value = PullCommentsOperation.class, name = "comments"),
        @JsonSubTypes.Type(value = PullRepliesOperation.class, name = "replies"),
        @JsonSubTypes.Type(value = PullVideosOperation.class, name = "videos")
})
interface PullOperationMixin {

}

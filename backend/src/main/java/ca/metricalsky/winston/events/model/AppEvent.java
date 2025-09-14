package ca.metricalsky.winston.events.model;

import ca.metricalsky.winston.api.model.Problem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class AppEvent {

    @JsonProperty("event_id")
    private final UUID eventId = UUID.randomUUID();

    @JsonProperty("event_type")
    private final String eventType;

    @JsonProperty("error")
    private Problem error;
}

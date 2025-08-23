package ca.metricalsky.winston.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AppEvent {

    @JsonProperty("event_id")
    private final UUID eventId = UUID.randomUUID();

    @JsonProperty("event_type")
    private final String eventType;
}

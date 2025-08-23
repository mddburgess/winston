package ca.metricalsky.winston.events;

import ca.metricalsky.winston.config.exception.AppProblemDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AppEvent {

    @JsonProperty("event_id")
    private final UUID eventId = UUID.randomUUID();

    @JsonProperty("event_type")
    private final String eventType;

    @JsonProperty("error")
    private AppProblemDetail error;
}

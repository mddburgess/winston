package ca.metricalsky.winston.events;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PullOperationEvent extends AppEvent {

    private static final String EVENT_TYPE = "pull-operation";
    private FetchOperationEntity operation;

    public PullOperationEvent() {
        super(EVENT_TYPE);
    }
}

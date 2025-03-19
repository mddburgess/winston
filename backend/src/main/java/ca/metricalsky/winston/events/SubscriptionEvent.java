package ca.metricalsky.winston.events;

import java.util.UUID;

public record SubscriptionEvent(boolean connected, UUID subscriptionId) {

}

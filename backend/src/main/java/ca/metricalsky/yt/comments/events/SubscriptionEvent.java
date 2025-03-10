package ca.metricalsky.yt.comments.events;

import java.util.UUID;

public record SubscriptionEvent(boolean connected, UUID subscriptionId) {

}

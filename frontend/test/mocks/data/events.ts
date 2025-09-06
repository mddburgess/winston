import { faker } from "@faker-js/faker";
import type { Channel, Problem } from "#/api";
import type {
  EventSubscriptionEvent,
  PullOperationEvent,
  PullRequestEvent,
  PullResultsEvent,
} from "#/components/events/types";
import type { PullOperationRead } from "#/types";

const eventSubscriptionEvent = (eventSubscriptionId: string, subscribed: boolean): EventSubscriptionEvent => ({
  event_id: faker.string.uuid(),
  event_type: "event-subscription",
  event_subscription_id: eventSubscriptionId,
  subscribed,
});

const pullRequestEvent = (): PullRequestEvent => ({
  event_id: faker.string.uuid(),
  event_type: "pull-request",
  request: {
    event_subscription_id: faker.string.uuid(),
    operations: [] as PullOperationRead[],
    status: "accepted",
    id: faker.string.numeric(),
  },
});

type PullOperationEventOptions = {
  channelHandle?: string;
  error?: Problem;
};

const pullOperationEvent = (options?: PullOperationEventOptions): PullOperationEvent => ({
  event_id: faker.string.uuid(),
  event_type: "pull-operation",
  operation: {
    pull: "channel",
    channel_handle: options?.channelHandle ?? `@${faker.internet.username()}`,
    status: options?.error ? "failed" : "successful",
    id: faker.string.numeric(),
  },
  error: options?.error,
});

const pullChannelsEvent = (channel?: Channel): PullResultsEvent<Channel> => ({
  event_id: faker.string.uuid(),
  event_type: "pull-results",
  operation: {
    pull: "channel",
    channel_handle: `@${faker.internet.username()}`,
    status: "fetching",
    id: faker.string.numeric(),
  },
  results: channel ? { count: 1, total_count: 1, items: [channel] } : { count: 0, total_count: 0, items: [] },
});

export { eventSubscriptionEvent, pullChannelsEvent, pullOperationEvent, pullRequestEvent };

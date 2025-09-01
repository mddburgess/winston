import { faker } from "@faker-js/faker";
import type {
  EventSubscriptionEvent,
  PullChannelsEvent,
  PullCommentsEvent,
  PullOperationEvent,
  PullRepliesEvent,
  PullRequestEvent,
  PullVideosEvent,
} from "#/components/events/types";
import type { PullOperationRead } from "#/types";
import type { Channel } from "#/api";

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

const pullOperationEvent = (): PullOperationEvent => ({
  event_id: faker.string.uuid(),
  event_type: "pull-operation",
  operation: {
    pull: "channel",
    channel_handle: `@${faker.internet.username()}`,
    status: "ready",
    id: faker.string.numeric(),
  },
});

const pullChannelsEvent = (channel?: Channel): PullChannelsEvent => ({
  event_id: faker.string.uuid(),
  event_type: "pull-channels",
  channel_handle: `@${faker.internet.username()}`,
  channels: channel ? [channel] : [],
});

const pullVideosEvent = (): PullVideosEvent => ({
  event_id: faker.string.uuid(),
  event_type: "pull-videos",
  channel_handle: `@${faker.internet.username()}`,
  videos: [],
});

const pullCommentsEvent = (): PullCommentsEvent => ({
  event_id: faker.string.uuid(),
  event_type: "pull-comments",
  video_id: faker.string.nanoid(),
  comments: [],
});

const pullRepliesEvent = (): PullRepliesEvent => ({
  event_id: faker.string.uuid(),
  event_type: "pull-replies",
  video_id: faker.string.nanoid(),
  comment_id: faker.string.nanoid(),
  replies: [],
});

export {
  eventSubscriptionEvent,
  pullChannelsEvent,
  pullCommentsEvent,
  pullOperationEvent,
  pullRepliesEvent,
  pullRequestEvent,
  pullVideosEvent,
};

import type { Channel, Comment, Problem, Video } from "#/api";
import type { PullOperationRead, PullRequestRead, TopLevelComment } from "#/types";

type AppEvent = {
  event_id: string;
};

type EventSubscriptionEvent = AppEvent & {
  event_type: "event-subscription";
  event_subscription_id: string;
  subscribed: boolean;
  error?: Problem;
};

type PullRequestEvent = AppEvent & {
  event_type: "pull-request";
  request: PullRequestRead;
  error?: Problem;
};

type PullOperationEvent = AppEvent & {
  event_type: "pull-operation";
  operation: PullOperationRead;
  error?: Problem;
};

type PullChannelsEvent = AppEvent & {
  event_type: "pull-channels";
  channel_handle: Channel["handle"];
  channels: Channel[];
};

type PullVideosEvent = AppEvent & {
  event_type: "pull-videos";
  channel_handle: Channel["handle"];
  videos: Video[];
};

type PullCommentsEvent = AppEvent & {
  event_type: "pull-comments";
  video_id: Video["id"];
  comments: TopLevelComment[];
};

type PullRepliesEvent = AppEvent & {
  event_type: "pull-replies";
  video_id: Video["id"];
  comment_id: TopLevelComment["id"];
  comments: Comment[];
};

export type {
  EventSubscriptionEvent,
  PullChannelsEvent,
  PullCommentsEvent,
  PullOperationEvent,
  PullRepliesEvent,
  PullRequestEvent,
  PullVideosEvent,
};

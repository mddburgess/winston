import type { Channel, Comment, Problem, Video } from "#/api";
import type { PullOperationRead, PullRequestRead, TopLevelComment } from "#/types";

type AppEvent =
  | EventSubscriptionEvent
  | PullRequestEvent
  | PullOperationEvent
  | PullChannelsEvent
  | PullVideosEvent
  | PullCommentsEvent
  | PullRepliesEvent;

type EventSubscriptionEvent = {
  event_id: string;
  event_type: "event-subscription";
  event_subscription_id: string;
  subscribed: boolean;
  error?: Problem;
};

type PullRequestEvent = {
  event_id: string;
  event_type: "pull-request";
  request: PullRequestRead;
  error?: Problem;
};

type PullOperationEvent = {
  event_id: string;
  event_type: "pull-operation";
  operation: PullOperationRead;
  error?: Problem;
};

type PullChannelsEvent = {
  event_id: string;
  event_type: "pull-channels";
  channel_handle: Channel["handle"];
  channels: Channel[];
};

type PullVideosEvent = {
  event_id: string;
  event_type: "pull-videos";
  channel_handle: Channel["handle"];
  videos: Video[];
};

type PullCommentsEvent = {
  event_id: string;
  event_type: "pull-comments";
  video_id: Video["id"];
  comments: TopLevelComment[];
};

type PullRepliesEvent = {
  event_id: string;
  event_type: "pull-replies";
  video_id: Video["id"];
  comment_id: TopLevelComment["id"];
  replies: Comment[];
};

export type {
  AppEvent,
  EventSubscriptionEvent,
  PullChannelsEvent,
  PullCommentsEvent,
  PullOperationEvent,
  PullRepliesEvent,
  PullRequestEvent,
  PullVideosEvent,
};

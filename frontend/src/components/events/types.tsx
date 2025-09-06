import type { Channel, Comment, Problem, Video } from "#/api";
import type { PullOperationRead, PullRequestRead, TopLevelComment } from "#/types";

type AppEvent =
  | EventSubscriptionEvent
  | PullRequestEvent
  | PullOperationEvent
  | PullResultsEvent<Channel | Video | TopLevelComment | Comment>;

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

type PullResultsEvent<T> = {
  event_id: string;
  event_type: "pull-results";
  operation: PullOperationRead;
  results: {
    count: number;
    total_count: number;
    items: T[];
  };
};

export type { AppEvent, EventSubscriptionEvent, PullOperationEvent, PullRequestEvent, PullResultsEvent };

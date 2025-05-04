import type { Channel } from "./channels";
import type { Comment } from "./comments";
import type { ProblemDetail } from "./errors";
import type { Video } from "./videos";

type FetchDataEvent<T> = {
    objectId: string;
    items: T[];
};

type FetchCompletedEvent = {
    status: "COMPLETED";
};

type FetchFailedEvent = {
    status: "FAILED";
    error: ProblemDetail;
};

export type SubscriptionEvent = {
    connected: boolean;
    subscriptionId: string;
};

export type FetchChannelEvent = FetchDataEvent<Channel>;
export type FetchCommentsEvent = FetchDataEvent<Comment>;
export type FetchVideosEvent = FetchDataEvent<Video>;
export type FetchStatusEvent = FetchCompletedEvent | FetchFailedEvent;

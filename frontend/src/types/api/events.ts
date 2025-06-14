import type { Channel, Video } from "#/api";
import type { TopLevelComment } from "#/store/slices/backend";
import type { ProblemDetail } from "#/types";

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

type SubscriptionEvent = {
    connected: boolean;
    subscriptionId: string;
};

type FetchChannelEvent = FetchDataEvent<Channel>;
type FetchCommentsEvent = FetchDataEvent<TopLevelComment>;
type FetchVideosEvent = FetchDataEvent<Video>;
type FetchStatusEvent = FetchCompletedEvent | FetchFailedEvent;

export type {
    FetchChannelEvent,
    FetchCommentsEvent,
    FetchStatusEvent,
    FetchVideosEvent,
    SubscriptionEvent,
};

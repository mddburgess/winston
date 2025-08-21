import type { Channel, Video } from "#/api";
import type { ProblemDetail, TopLevelComment } from "#/types";

type FetchDataEvent<T> = {
    objectId: string;
    items: T[];
};

type SubscriptionEvent = {
    connected: boolean;
    subscriptionId: string;
};

type FetchChannelEvent = FetchDataEvent<Channel>;
type FetchCommentsEvent = FetchDataEvent<TopLevelComment>;
type FetchVideosEvent = FetchDataEvent<Video>;

type FetchStatusEvent = {
    operation?: {
        operationType: "CHANNELS" | "VIDEOS" | "COMMENTS" | "REPLIES";
        objectId: string;
        status: "READY" | "FETCHING" | "SUCCESSFUL" | "FAILED";
    };
    status?: "COMPLETED" | "FAILED";
    error?: ProblemDetail;
};

export type {
    FetchChannelEvent,
    FetchCommentsEvent,
    FetchStatusEvent,
    FetchVideosEvent,
    SubscriptionEvent,
};

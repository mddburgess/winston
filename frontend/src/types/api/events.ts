import type { Channel, Problem, PullOperationRead, Video } from "#/api";
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

type AppEvent = {
    event_id?: string;
    event_type?: string;
    operation?: PullOperationRead;
    channel?: Channel;
    videos?: Video[];
    comments?: TopLevelComment[];
    replies?: Comment[];
    error?: Problem;
};

export type {
    AppEvent,
    FetchChannelEvent,
    FetchCommentsEvent,
    FetchStatusEvent,
    FetchVideosEvent,
    SubscriptionEvent,
};

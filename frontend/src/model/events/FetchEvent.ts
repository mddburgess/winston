import type { ChannelDto } from "../ChannelDto";
import type { CommentDto } from "../CommentDto";
import type { VideoWithChannelIdDto } from "../VideoDto";
import type { ProblemDetail } from "./ProblemDetail";

type FetchDataEvent<T> = {
    objectId: string;
    items: T[];
};

export type FetchChannelEvent = FetchDataEvent<ChannelDto>;
export type FetchVideosEvent = FetchDataEvent<VideoWithChannelIdDto>;
export type FetchCommentsEvent = FetchDataEvent<CommentDto>;

type FetchCompletedEvent = {
    status: "COMPLETED";
};

type FetchFailedEvent = {
    status: "FAILED";
    error: ProblemDetail;
};

export type FetchStatusEvent = FetchCompletedEvent | FetchFailedEvent;

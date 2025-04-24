import { ChannelDto } from "../ChannelDto";
import { VideoWithChannelIdDto } from "../VideoDto";
import { CommentDto } from "../CommentDto";
import { ProblemDetail } from "./ProblemDetail";

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

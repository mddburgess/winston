import {ChannelDto} from "../ChannelDto";
import {VideoWithChannelIdDto} from "../VideoDto";
import {CommentDto} from "../CommentDto";
import {ProblemDetail} from "./ProblemDetail";


type FetchDataEvent<T> = {
    status: 'FETCHING' | 'COMPLETED';
    items: T[];
    error: undefined;
}

type FetchErrorEvent = {
    status: 'FAILED';
    items: undefined;
    error: ProblemDetail;
}

type FetchEvent<T> = {
    objectId: string;
} & (FetchDataEvent<T> | FetchErrorEvent);

export type FetchChannelEvent = FetchEvent<ChannelDto>;
export type FetchVideosEvent = FetchEvent<VideoWithChannelIdDto>;
export type FetchCommentsEvent = FetchEvent<CommentDto>;

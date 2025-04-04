import {ChannelDto} from "../ChannelDto";
import {VideoWithChannelIdDto} from "../VideoDto";
import {CommentDto} from "../CommentDto";

export type FetchEvent<T> = {
    objectId: string;
    status: 'FETCHING' | 'COMPLETED';
    items: T[];
}

export type FetchChannelEvent = FetchEvent<ChannelDto>;
export type FetchVideosEvent = FetchEvent<VideoWithChannelIdDto>;
export type FetchCommentsEvent = FetchEvent<CommentDto>;

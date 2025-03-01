import {ChannelDto} from "./ChannelDto";

export type VideoDto = {
    id: string;
    channelId?: string;
    channel?: ChannelDto
    title?: string;
    description?: string;
    thumbnailUrl?: string;
    commentCount?: number;
    replyCount?: number;
    totalReplyCount?: number;
    publishedAt?: string;
    lastFetchedAt?: string;
}

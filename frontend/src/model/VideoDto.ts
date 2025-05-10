import type { ChannelDto } from "./ChannelDto";

type VideoDto = {
    id: string;
    channelId: string;
    channel: ChannelDto;
    title: string;
    description: string;
    thumbnailUrl: string;
    commentCount: number;
    replyCount: number;
    totalReplyCount: number;
    commentsDisabled: boolean;
    publishedAt: string;
    lastFetchedAt: string;
};

export type VideoWithChannelIdDto = Omit<VideoDto, "channel">;

export type VideoWithChannelDto = Omit<VideoDto, "channelId">;

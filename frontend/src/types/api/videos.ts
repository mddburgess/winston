import type { Channel } from "./channels";

export type Video = {
    id: string;
    channelId: string;
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

export type VideoProps = {
    video: Video;
};

export type VideoListProps = {
    videos: Video[];
};

export type VideoWithChannel = Omit<Video, "channelId"> & {
    channel: Channel;
};

export type VideoWithChannelProps = {
    video: VideoWithChannel;
};

export type VideoListResponse = Video[];

export type VideoDetailsResponse = VideoWithChannel;

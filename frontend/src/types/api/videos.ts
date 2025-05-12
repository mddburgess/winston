import type { Channel } from "#/types";

type Video = {
    id: string;
    channelId: string;
    title: string;
    description: string;
    thumbnailUrl: string;
    comments?: {
        commentsDisabled: boolean;
        commentCount: number;
        replyCount: number;
        totalReplyCount: number;
        lastFetchedAt: string;
    };
    publishedAt: string;
    lastFetchedAt: string;
};

type VideoProps = {
    video: Video;
};

type VideoListProps = {
    videos: Video[];
};

type VideoWithChannel = Omit<Video, "channelId"> & {
    channel: Channel;
};

type VideoWithChannelProps = {
    video: VideoWithChannel;
};

type VideoListResponse = Video[];

type VideoDetailsResponse = VideoWithChannel;

export type {
    Video,
    VideoDetailsResponse,
    VideoListProps,
    VideoListResponse,
    VideoProps,
    VideoWithChannel,
    VideoWithChannelProps,
};

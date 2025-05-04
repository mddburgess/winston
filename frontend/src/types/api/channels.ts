export type Channel = {
    id: string;
    title: string;
    description: string;
    customUrl: string;
    thumbnailUrl: string;
    topics: string[];
    keywords: string[];
    videoCount: number;
    publishedAt: string;
    lastFetchedAt: string;
};

export type ChannelProps = {
    channel: Channel;
};

export type ChannelListProps = {
    channels: Channel[];
};

export type ChannelListResponse = Channel[];

export type ChannelDetailResponse = Channel;

type Channel = {
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

type ChannelProps = {
    channel: Channel;
};

type ChannelListProps = {
    channels: Channel[];
};

type ChannelListResponse = Channel[];

type ChannelDetailResponse = Channel;

export type {
    Channel,
    ChannelDetailResponse,
    ChannelListProps,
    ChannelListResponse,
    ChannelProps,
};

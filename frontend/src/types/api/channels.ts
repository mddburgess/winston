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

export type { Channel, ChannelListProps, ChannelProps };

export type ChannelDto = {
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
}

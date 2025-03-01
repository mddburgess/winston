export type VideoDto = {
    id: string;
    channelId?: string;
    title?: string;
    description?: string;
    thumbnailUrl?: string;
    commentCount?: number;
    replyCount?: number;
    totalReplyCount?: number;
    publishedAt?: string;
    lastFetchedAt?: string;
}

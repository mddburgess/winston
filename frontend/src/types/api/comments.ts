import type { Author } from "./authors";

export type Comment = {
    id: string;
    videoId: string;
    author: Author;
    text: string;
    publishedAt: string;
    updateAt: string;
    lastFetchedAt: string;
    totalReplyCount: number;
    replies: Comment[];
};

export type CommentListResponse = Comment[];

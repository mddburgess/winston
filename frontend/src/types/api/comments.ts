import type { Author } from "#/types";

type Comment = {
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

type CommentListResponse = Comment[];

export type { Comment, CommentListResponse };

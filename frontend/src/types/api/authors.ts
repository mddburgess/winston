import type { Comment } from "./comments";
import type { Video } from "./videos";

export type Author = {
    id: string;
    displayName: string;
    channelUrl: string;
    profileImageUrl: string;
};

export type AuthorWithStatistics = Author & {
    statistics: {
        commentedVideos: number;
        totalComments: number;
        totalReplies: number;
    };
};

export type AuthorListResponse = {
    results: number;
    authors: AuthorWithStatistics[];
};

export type AuthorDetailsResponse = {
    author: Author;
    comments: Comment[];
    videos: Video[];
};

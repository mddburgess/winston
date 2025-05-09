import type { Channel } from "./channels";
import type { Video } from "./videos";

type Author = {
    id: string;
    displayName: string;
    channelUrl: string;
    profileImageUrl: string;
};

type AuthorWithStatistics = Author & {
    statistics: {
        commentedVideos: number;
        totalComments: number;
        totalReplies: number;
    };
};

type AuthorListResponse = {
    results: number;
    authors: AuthorWithStatistics[];
};

type AuthorSummaryResponse = {
    author: Author;
    channels: Channel[];
    videos: Video[];
};

type AuthorHandleProps = {
    authorHandle: string;
};

export type {
    Author,
    AuthorWithStatistics,
    AuthorListResponse,
    AuthorSummaryResponse,
    AuthorHandleProps,
};

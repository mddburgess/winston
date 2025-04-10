export type AuthorDto = {
    id: string;
    displayName: string;
    channelUrl: string;
    profileImageUrl: string;
}

export type AuthorWithStatisticsDto = AuthorDto & {
    statistics: AuthorStatistics;
}

type AuthorStatistics = {
    commentedVideos: number;
    totalComments: number;
    totalReplies: number;
}

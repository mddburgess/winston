export type AuthorDto = {
    id: string;
    displayName: string;
    channelUrl: string;
    profileImageUrl: string;
    statistics?: AuthorStatistics;
}

type AuthorStatistics = {
    commentedVideos: number;
    totalComments: number;
    totalReplies: number;
}

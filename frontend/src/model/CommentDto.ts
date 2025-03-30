import {AuthorDto} from "./AuthorDto";

export type CommentDto = {
    id: string,
    videoId: string,
    author: AuthorDto,
    text: string,
    publishedAt: string,
    updateAt: string,
    lastFetchedAt: string,
    totalReplyCount: number,
    replies: CommentDto[]
}

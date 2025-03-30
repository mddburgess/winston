import {AuthorDto} from "./AuthorDto";

export type CommentDto = {
    id: string,
    author: AuthorDto,
    text: string,
    publishedAt: string,
    updateAt: string,
    lastFetchedAt: string,
    totalReplyCount: number,
    replies: CommentDto[]
}

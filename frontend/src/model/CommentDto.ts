export type CommentDto = {
    id: string,
    author?: {
        id?: string,
        displayName?: string
    },
    text?: string,
    publishedAt?: string,
    updateAt?: string,
    lastFetchedAt?: string,
    replies?: CommentDto[]
}

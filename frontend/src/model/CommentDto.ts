export type CommentDto = {
    id: string,
    author?: {
        displayName?: string
    },
    text?: string,
    publishedAt?: string,
    updateAt?: string,
    lastFetchedAt?: string
}

import {CommentDto} from "../CommentDto";

export type FetchCommentsEvent = {
    videoId: string;
    status: 'FETCHING' | 'COMPLETED';
    comments: CommentDto[];
}

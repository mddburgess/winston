import {apiSlice} from "./api";
import {createEntityAdapter, EntityState} from "@reduxjs/toolkit";
import {CommentDto} from "../../model/CommentDto";
import {ascBy} from "../../utils";
import {DateTime} from "luxon";

export type CommentState = Omit<CommentDto, 'replies'> & {
    replies: EntityState<CommentDto, string>;
}

export const commentsAdapter = createEntityAdapter<CommentState>({
    sortComparer: ascBy(comment => DateTime.fromISO(comment.publishedAt).valueOf()),
})

export const repliesAdapter = createEntityAdapter<CommentDto>({
    sortComparer: ascBy(comment => DateTime.fromISO(comment.publishedAt).valueOf()),
})

export const commentsApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listCommentsByVideoId: builder.query<EntityState<CommentState, string>, string>({
            query: (videoId) => `/videos/${videoId}/comments`,
            transformResponse: (response: CommentDto[]) => {
                const comments = response.map(comment => ({
                    ...comment,
                    replies: repliesAdapter.addMany(repliesAdapter.getInitialState(), comment.replies)
                }))
                return commentsAdapter.addMany(commentsAdapter.getInitialState(), comments);
            }
        }),

    }),
    overrideExisting: 'throw'
})

export const {
    useListCommentsByVideoIdQuery,
    util: commentsApiUtils,
} = commentsApi;

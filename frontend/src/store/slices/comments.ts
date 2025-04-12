import {apiSlice} from "./api";
import {createEntityAdapter, EntityState} from "@reduxjs/toolkit";
import {CommentDto} from "../../model/CommentDto";
import {ascBy} from "../../utils";
import {DateTime} from "luxon";

export const commentsAdapter = createEntityAdapter<CommentDto>({
    sortComparer: ascBy(comment => DateTime.fromISO(comment.publishedAt).valueOf()),
})

export const commentsApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listCommentsByVideoId: builder.query<EntityState<CommentDto, string>, string>({
            query: (videoId) => `/videos/${videoId}/comments`,
            transformResponse: (response: CommentDto[]) => {
                return commentsAdapter.addMany(commentsAdapter.getInitialState(), response);
            }
        }),

    }),
    overrideExisting: 'throw'
})

export const {
    useListCommentsByVideoIdQuery,
    util: commentsApiUtils,
} = commentsApi;

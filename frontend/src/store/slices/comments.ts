import { apiSlice } from "./api";
import type { EntityState } from "@reduxjs/toolkit";
import { createEntityAdapter } from "@reduxjs/toolkit";
import type { Comment, CommentListResponse } from "../../types";
import { ascBy } from "../../utils";
import { DateTime } from "luxon";
import { api } from "../../utils/links";

type ListCommentsByVideoIdAuthorParams = {
    videoId: string;
    authorHandle: string;
};

const responseTransformer = (response: CommentListResponse) => {
    const comments = response.map((comment) => ({
        ...comment,
        replies: repliesAdapter.addMany(
            repliesAdapter.getInitialState(),
            comment.replies,
        ),
    }));
    return commentsAdapter.addMany(commentsAdapter.getInitialState(), comments);
};

export type CommentState = Omit<Comment, "replies"> & {
    replies: EntityState<Comment, string>;
};

export const commentsAdapter = createEntityAdapter<CommentState>({
    sortComparer: ascBy((comment) =>
        DateTime.fromISO(comment.publishedAt).valueOf(),
    ),
});

export const repliesAdapter = createEntityAdapter<Comment>({
    sortComparer: ascBy((comment) =>
        DateTime.fromISO(comment.publishedAt).valueOf(),
    ),
});

export const commentsApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listCommentsByVideoId: builder.query<
            EntityState<CommentState, string>,
            string
        >({
            query: api.v1.videos.id.comments.get,
            transformResponse: responseTransformer,
        }),
        listCommentsByVideoIdAuthor: builder.query<
            EntityState<CommentState, string>,
            ListCommentsByVideoIdAuthorParams
        >({
            query: ({ videoId, authorHandle }) => ({
                url: api.v1.videos.id.comments.get(videoId),
                params: {
                    author: authorHandle,
                },
            }),
            transformResponse: responseTransformer,
        }),
    }),
    overrideExisting: "throw",
});

export const {
    useListCommentsByVideoIdQuery,
    useListCommentsByVideoIdAuthorQuery,
    util: commentsApiUtils,
} = commentsApi;

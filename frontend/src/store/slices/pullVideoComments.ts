import { createSlice } from "@reduxjs/toolkit";
import { keyBy, map, mapValues, uniq } from "lodash";
import type { Comment, Video } from "#/api";
import type { PullOperationStatus, TopLevelComment } from "#/types";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { Dictionary } from "lodash";

type PullState = {
    status: PullOperationStatus;
    items: string[];
};

type VideoCommentsState = {
    comments: PullState;
    replies: PullState;
};

type PullVideoCommentsState = {
    active: Video[];
    videos: Dictionary<VideoCommentsState>;
};

const initialState: PullVideoCommentsState = {
    active: [],
    videos: {},
};

const pullVideoCommentsSlice = createSlice({
    name: "pulls",
    initialState,
    reducers: {
        requestPullComments: (state, action: PayloadAction<Video[]>) => {
            state.active = action.payload;
            state.videos = mapValues(
                keyBy(action.payload, "id"),
                (): VideoCommentsState => ({
                    comments: {
                        status: "ready",
                        items: [],
                    },
                    replies: {
                        status: "ready",
                        items: [],
                    },
                }),
            );
        },
        updatePullCommentsStatus: (
            state,
            action: PayloadAction<{
                videoId: string;
                commentsStatus?: PullOperationStatus;
                repliesStatus?: PullOperationStatus;
            }>,
        ) => {
            const { videoId, commentsStatus, repliesStatus } = action.payload;
            if (commentsStatus) {
                state.videos[videoId].comments.status = commentsStatus;
            }
            if (repliesStatus) {
                state.videos[videoId].replies.status = repliesStatus;
            }
        },
        updatePullCommentsData: (
            state,
            action: PayloadAction<{
                videoId: string;
                comments?: TopLevelComment[];
                replies?: Comment[];
            }>,
        ) => {
            const { videoId, comments, replies } = action.payload;

            const existingCommentIds = state.videos[videoId].comments.items;
            const existingReplyIds = state.videos[videoId].replies.items;

            const newCommentIds: string[] = map(comments ?? [], "id");
            const newCommentReplyIds = (comments ?? [])
                .flatMap((comment) => comment.replies ?? [])
                .map((reply) => reply.id);
            const newReplyIds = map(replies ?? [], "id");

            const commentIds = uniq([...existingCommentIds, ...newCommentIds]);
            const replyIds = uniq([
                ...existingReplyIds,
                ...newCommentReplyIds,
                ...newReplyIds,
            ]);

            state.videos[videoId] = {
                comments: {
                    status: state.videos[videoId].comments.status,
                    items: commentIds,
                },
                replies: {
                    status: state.videos[videoId].replies.status,
                    items: replyIds,
                },
            };
        },
        clearPullComments: (state) => {
            state.active = [];
            state.videos = {};
        },
    },
});

const pullVideoCommentsReducer = pullVideoCommentsSlice.reducer;
const {
    clearPullComments,
    requestPullComments,
    updatePullCommentsData,
    updatePullCommentsStatus,
} = pullVideoCommentsSlice.actions;

export {
    clearPullComments,
    pullVideoCommentsReducer,
    requestPullComments,
    updatePullCommentsData,
    updatePullCommentsStatus,
};
export type { PullVideoCommentsState, VideoCommentsState };

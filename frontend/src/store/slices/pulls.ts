import { createEntityAdapter, createSlice } from "@reduxjs/toolkit";
import { uniq } from "lodash";
import { DateTime } from "luxon";
import { descBy } from "#/utils";
import type { Comment, Video } from "#/api";
import type { TopLevelComment } from "#/store/slices/backend";
import type { Maybe } from "#/types";
import type { EntityState, PayloadAction } from "@reduxjs/toolkit";

type PullCommentsState = {
    video: Video;
    commentIds: string[];
    replyIds: string[];
    status: "READY" | "FETCHING" | "SUCCESSFUL" | "FAILED";
};

const pullCommentsAdapter = createEntityAdapter<PullCommentsState, string>({
    selectId: (state: PullCommentsState) => state.video.id,
    sortComparer: descBy((state) =>
        DateTime.fromISO(state.video.published_at).valueOf(),
    ),
});

const { selectAll: selectAllPullComments } = pullCommentsAdapter.getSelectors();

type PullStates = {
    batchPullComments: {
        [id: string]: Maybe<EntityState<PullCommentsState, string>>;
    };
};

const initialState: PullStates = {
    batchPullComments: {},
};

type BatchPullCommentsAction = {
    channelId: string;
    videos: Video[];
};

type BatchPullCommentsData = {
    channelId: string;
    videoId: string;
    comments?: TopLevelComment[];
    replies?: Comment[];
};

type BatchPullCommentsStatus = {
    channelId: string;
    videoId: string;
    status: "READY" | "FETCHING" | "SUCCESSFUL" | "FAILED";
};

const pullsSlice = createSlice({
    name: "pulls",
    initialState,
    reducers: {
        batchPullComments: (
            state: PullStates,
            action: PayloadAction<BatchPullCommentsAction>,
        ) => {
            const pullComments: PullCommentsState[] = action.payload.videos.map(
                (video) => ({
                    video: video,
                    commentIds: [],
                    replyIds: [],
                    status: "READY",
                }),
            );
            state.batchPullComments[action.payload.channelId] =
                pullCommentsAdapter.addMany(
                    pullCommentsAdapter.getInitialState(),
                    pullComments,
                );
        },
        batchPullCommentsData: (
            state,
            action: PayloadAction<BatchPullCommentsData>,
        ) => {
            const pullComments =
                state.batchPullComments[action.payload.channelId] ??
                pullCommentsAdapter.getInitialState();
            const videoState = pullCommentsAdapter
                .getSelectors()
                .selectById(pullComments, action.payload.videoId);

            const newCommentIds = (action.payload.comments ?? []).map(
                (comment) => comment.id,
            );
            const newCommentReplyIds = (action.payload.comments ?? [])
                .flatMap((comment) => comment.replies ?? [])
                .map((reply) => reply.id);
            const newReplyIds = (action.payload.replies ?? []).map(
                (reply) => reply.id,
            );

            const commentIds = uniq([
                ...videoState.commentIds,
                ...newCommentIds,
            ]);
            const replyIds = uniq([
                ...videoState.replyIds,
                ...newCommentReplyIds,
                ...newReplyIds,
            ]);

            state.batchPullComments[action.payload.channelId] =
                pullCommentsAdapter.setOne(pullComments, {
                    ...videoState,
                    commentIds,
                    replyIds,
                });
        },
        batchPullCommentsStatus: (
            state,
            action: PayloadAction<BatchPullCommentsStatus>,
        ) => {
            const pullComments =
                state.batchPullComments[action.payload.channelId] ??
                pullCommentsAdapter.getInitialState();
            const videoState = pullCommentsAdapter
                .getSelectors()
                .selectById(pullComments, action.payload.videoId);

            state.batchPullComments[action.payload.channelId] =
                pullCommentsAdapter.setOne(pullComments, {
                    ...videoState,
                    status: action.payload.status,
                });
        },
        clearBatchPullComments: (state, action: PayloadAction<string>) => {
            state.batchPullComments[action.payload] = undefined;
        },
    },
});

const pulls = pullsSlice.reducer;
const {
    batchPullComments,
    batchPullCommentsData,
    batchPullCommentsStatus,
    clearBatchPullComments,
} = pullsSlice.actions;

export {
    batchPullComments,
    batchPullCommentsData,
    batchPullCommentsStatus,
    clearBatchPullComments,
    pulls,
    selectAllPullComments,
};
export type { PullCommentsState };

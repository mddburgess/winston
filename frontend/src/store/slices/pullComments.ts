import { createSlice } from "@reduxjs/toolkit";
import { isArray, union } from "lodash";
import type { Problem } from "#/api";
import type { PullOperationStatus } from "#/types";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { Dictionary } from "lodash";

type PullCommentsResponse = {
  videoId: string;
  commentStatus: PullOperationStatus;
  commentCount: number;
  replyStatus: PullOperationStatus;
  replyIds: string[];
};

type PullCommentsState = {
  active: boolean;
  requested: string[];
  responses: Partial<Dictionary<PullCommentsResponse>>;
  errors: Partial<Dictionary<Problem>>;
};

const initialState: PullCommentsState = {
  active: false,
  requested: [],
  responses: {},
  errors: {},
};

const pullComments = createSlice({
  name: "pullComments",
  initialState,
  reducers: {
    pullCommentsActive: (state, { payload }: PayloadAction<boolean>) => {
      state.active = payload;
    },
    pullCommentsRequested: (state, { payload }: PayloadAction<string | string[]>) => {
      state.active = true;
      state.requested = isArray(payload) ? payload : [payload];
    },
    pullCommentsResponse: (state, { payload }: PayloadAction<{ videoId: string } & Partial<PullCommentsResponse>>) => {
      const currentCommentStatus = state.responses[payload.videoId]?.commentStatus;
      const currentCommentCount = state.responses[payload.videoId]?.commentCount;
      const currentReplyStatus = state.responses[payload.videoId]?.replyStatus;
      const currentReplyIds = state.responses[payload.videoId]?.replyIds;

      state.responses[payload.videoId] = {
        videoId: payload.videoId,
        commentStatus: payload.commentStatus ?? currentCommentStatus ?? "ready",
        commentCount: (currentCommentCount ?? 0) + (payload.commentCount ?? 0),
        replyStatus: payload.replyStatus ?? currentReplyStatus ?? "ready",
        replyIds: union(currentReplyIds, payload.replyIds),
      };
    },
    pullCommentsError: (state, { payload }: PayloadAction<{ videoId: string; error: Problem }>) => {
      state.errors[payload.videoId] = payload.error;
    },
  },
});

const pullCommentsReducer = pullComments.reducer;
const { pullCommentsActive, pullCommentsError, pullCommentsRequested, pullCommentsResponse } = pullComments.actions;

export { pullCommentsActive, pullCommentsError, pullCommentsReducer, pullCommentsRequested, pullCommentsResponse };

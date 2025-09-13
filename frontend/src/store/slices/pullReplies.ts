import { createSlice } from "@reduxjs/toolkit";
import type { Problem } from "#/api";
import type { PullOperationStatus } from "#/types";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { Dictionary } from "lodash";

type PullRepliesResponse = {
  commentId: string;
  status: PullOperationStatus;
  count: number;
};

type PullRepliesState = {
  active: boolean;
  requested?: string;
  responses: Partial<Dictionary<PullRepliesResponse>>;
  errors: Partial<Dictionary<Problem>>;
};

const initialState: PullRepliesState = {
  active: false,
  requested: undefined,
  responses: {},
  errors: {},
};

const pullReplies = createSlice({
  name: "pullReplies",
  initialState,
  reducers: {
    pullRepliesActive: (state, { payload }: PayloadAction<boolean>) => {
      state.active = payload;
    },
    pullRepliesRequested: (state, { payload }: PayloadAction<string>) => {
      state.active = true;
      state.requested = payload;
    },
    pullRepliesResponse: (state, { payload }: PayloadAction<PullRepliesResponse>) => {
      const currentCount = state.responses[payload.commentId]?.count ?? 0;
      state.responses[payload.commentId] = {
        commentId: payload.commentId,
        status: payload.status,
        count: currentCount + payload.count,
      };
    },
    pullRepliesError: (state, { payload }: PayloadAction<{ commentId: string; error: Problem }>) => {
      state.errors[payload.commentId] = payload.error;
    },
  },
});

const pullRepliesReducer = pullReplies.reducer;
const { pullRepliesActive, pullRepliesError, pullRepliesRequested, pullRepliesResponse } = pullReplies.actions;

export { pullRepliesActive, pullRepliesError, pullRepliesReducer, pullRepliesRequested, pullRepliesResponse };

import { createSlice } from "@reduxjs/toolkit";
import type { Problem } from "#/api";
import type { PullOperationStatus } from "#/types";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { Dictionary } from "lodash";

type PullVideosRequest = {
  channelHandle: string;
  range: "all" | "latest";
};

type PullVideosResponse = {
  channelHandle: string;
  status: PullOperationStatus;
  count: number;
};

type PullVideosState = {
  active: boolean;
  requested?: PullVideosRequest;
  responses: Partial<Dictionary<PullVideosResponse>>;
  errors: Partial<Dictionary<Problem>>;
};

const initialState: PullVideosState = {
  active: false,
  requested: undefined,
  responses: {},
  errors: {},
};

const pullVideos = createSlice({
  name: "pullVideos",
  initialState,
  reducers: {
    pullVideosActive: (state, { payload }: PayloadAction<boolean>) => {
      state.active = payload;
    },
    pullVideosRequested: (state, { payload }: PayloadAction<PullVideosRequest>) => {
      state.active = true;
      state.requested = payload;
      state.responses[payload.channelHandle] = undefined;
    },
    pullVideosResponse: (state, { payload }: PayloadAction<PullVideosResponse>) => {
      const currentCount = state.responses[payload.channelHandle]?.count ?? 0;
      state.responses[payload.channelHandle] = {
        channelHandle: payload.channelHandle,
        status: payload.status,
        count: currentCount + payload.count,
      };
    },
    pullVideosError: (state, { payload }: PayloadAction<{ channelHandle: string; error: Problem }>) => {
      state.errors[payload.channelHandle] = payload.error;
    },
  },
});

const pullVideosReducer = pullVideos.reducer;
const { pullVideosActive, pullVideosError, pullVideosRequested, pullVideosResponse } = pullVideos.actions;

export { pullVideosActive, pullVideosError, pullVideosReducer, pullVideosRequested, pullVideosResponse };

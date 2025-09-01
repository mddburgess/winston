import { createSlice } from "@reduxjs/toolkit";
import { isArray } from "lodash";
import type { Problem } from "#/api";
import type { PullOperationStatus } from "#/types";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { Dictionary } from "lodash";

type PullChannelState = {
  active: boolean;
  requested: string[];
  responses: Partial<Dictionary<PullOperationStatus>>;
  errors: Partial<Dictionary<Problem>>;
};

const initialState: PullChannelState = {
  active: false,
  requested: [],
  responses: {},
  errors: {},
};

const pullChannel = createSlice({
  name: "pullChannel",
  initialState,
  reducers: {
    pullChannelActive: (state, { payload }: PayloadAction<boolean>) => {
      state.active = payload;
    },
    pullChannelRequested: (state, { payload }: PayloadAction<string | string[]>) => {
      state.active = true;
      state.requested = isArray(payload) ? payload : [payload];
    },
    pullChannelResponse: (
      state,
      { payload }: PayloadAction<{ channelHandle: string; status: PullOperationStatus }>,
    ) => {
      state.responses[payload.channelHandle] = payload.status;
    },
    pullChannelError: (state, { payload }: PayloadAction<{ channelHandle: string; error: Problem }>) => {
      state.errors[payload.channelHandle] = payload.error;
    },
  },
});

const pullChannelReducer = pullChannel.reducer;
const { pullChannelActive, pullChannelError, pullChannelRequested, pullChannelResponse } = pullChannel.actions;

export { pullChannelActive, pullChannelError, pullChannelReducer, pullChannelRequested, pullChannelResponse };

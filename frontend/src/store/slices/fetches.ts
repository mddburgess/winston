import { createSlice } from "@reduxjs/toolkit";
import type { Channel } from "#/api";
import type { AppEvent, FetchCommentsEvent, Maybe } from "#/types";
import type { PayloadAction } from "@reduxjs/toolkit";

type FetchStates = {
  comments: {
    [id: string]: Maybe<FetchState>;
  };
  replies: {
    [id: string]: Maybe<FetchState>;
  };
};

const initialState: FetchStates = {
  comments: {},
  replies: {},
};

type UpdateFetchStatus = {
  fetchType: keyof FetchStates;
  objectId: string;
  status: "COMPLETED" | "FAILED";
};

export type FetchState = {
  id: string;
  mode?: "all" | "latest";
  status: "READY" | "REQUESTED" | "FETCHING" | "COMPLETED" | "FAILED";
  count: number;
};

export const fetchesSlice = createSlice({
  name: "fetches",
  initialState,
  reducers: {
    requestedCommentsForVideoId: (state, action: PayloadAction<string>) => {
      state.comments[action.payload] = {
        id: action.payload,
        status: "REQUESTED",
        count: 0,
      };
    },
    fetchedComments: (state, action: PayloadAction<FetchCommentsEvent>) => {
      const event = action.payload;
      const fetchState = state.comments[event.objectId];
      state.comments[event.objectId] = {
        id: event.objectId,
        status: "FETCHING",
        count: (fetchState?.count ?? 0) + event.items.length,
      };
    },
    requestedRepliesForId: (state, action: PayloadAction<string>) => {
      state.replies[action.payload] = {
        id: action.payload,
        status: "REQUESTED",
        count: 0,
      };
    },
    fetchedReplies: (state, action: PayloadAction<AppEvent>) => {
      const event = action.payload;
      const fetchState = state.replies[event.object_id];
      state.replies[event.object_id] = {
        id: event.object_id,
        status: "FETCHING",
        count: (fetchState?.count ?? 0) + (event.replies?.length ?? 0),
      };
    },
    updateFetchStatus: (state, action: PayloadAction<UpdateFetchStatus>) => {
      const event = action.payload;
      const fetchState = state[event.fetchType][event.objectId];
      state[event.fetchType][event.objectId] = {
        id: event.objectId,
        status: event.status,
        count: fetchState?.count ?? 0,
      };
    },
  },
});

export const {
  requestedCommentsForVideoId,
  fetchedComments,
  requestedRepliesForId,
  fetchedReplies,
  updateFetchStatus,
} = fetchesSlice.actions;

export default fetchesSlice.reducer;

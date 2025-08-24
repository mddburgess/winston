import { createSlice } from "@reduxjs/toolkit";
import type { Channel } from "#/api";
import type { AppEvent, FetchCommentsEvent, Maybe } from "#/types";
import type { PayloadAction } from "@reduxjs/toolkit";

type FetchStates = {
    channel: {
        [id: string]: Maybe<FetchState>;
    };
    videos: {
        [id: string]: Maybe<FetchState>;
    };
    comments: {
        [id: string]: Maybe<FetchState>;
    };
    replies: {
        [id: string]: Maybe<FetchState>;
    };
};

const initialState: FetchStates = {
    channel: {},
    videos: {},
    comments: {},
    replies: {},
};

type FetchVideosRequest = {
    channelId: string;
    mode: "all" | "latest";
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
        requestedChannelForHandle: (state, action: PayloadAction<string>) => {
            state.channel[action.payload] = {
                id: action.payload,
                status: "REQUESTED",
                count: 0,
            };
        },
        initFetchStateForChannel: (state, action: PayloadAction<Channel>) => {
            state.videos[action.payload.id] = {
                id: action.payload.id,
                status: "READY",
                count: 0,
            };
        },
        requestedVideosForChannelId: (
            state,
            action: PayloadAction<FetchVideosRequest>,
        ) => {
            state.videos[action.payload.channelId] = {
                id: action.payload.channelId,
                mode: action.payload.mode,
                status: "REQUESTED",
                count: 0,
            };
        },
        fetchedVideos: (state, action: PayloadAction<AppEvent>) => {
            const event = action.payload;
            const fetchState = state.videos[event.object_id];
            state.videos[event.object_id] = {
                id: event.object_id,
                status: "FETCHING",
                count: (fetchState?.count ?? 0) + (event.videos?.length ?? 0),
            };
        },
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
        updateFetchStatus: (
            state,
            action: PayloadAction<UpdateFetchStatus>,
        ) => {
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
    requestedChannelForHandle,
    initFetchStateForChannel,
    requestedVideosForChannelId,
    fetchedVideos,
    requestedCommentsForVideoId,
    fetchedComments,
    requestedRepliesForId,
    fetchedReplies,
    updateFetchStatus,
} = fetchesSlice.actions;

export default fetchesSlice.reducer;

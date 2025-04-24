import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { ChannelDto } from "../../model/ChannelDto";
import {
    FetchCommentsEvent,
    FetchVideosEvent,
} from "../../model/events/FetchEvent";

export type FetchState = {
    id: string;
    mode?: "ALL" | "LATEST";
    status: "READY" | "REQUESTED" | "FETCHING" | "COMPLETED" | "FAILED";
    count: number;
};

type FetchStates = {
    channel: {
        [id: string]: FetchState;
    };
    videos: {
        [id: string]: FetchState;
    };
    comments: {
        [id: string]: FetchState;
    };
    replies: {
        [id: string]: FetchState;
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
    mode: "ALL" | "LATEST";
};

type UpdateFetchStatus = {
    fetchType: keyof FetchStates;
    objectId: string;
    status: "COMPLETED" | "FAILED";
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
        initFetchStateForChannel: (
            state,
            action: PayloadAction<ChannelDto>,
        ) => {
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
        fetchedVideos: (state, action: PayloadAction<FetchVideosEvent>) => {
            const event = action.payload;
            const fetchState = state.videos[event.objectId];
            state.videos[event.objectId] = {
                id: event.objectId,
                status: "FETCHING",
                count: fetchState.count + event.items.length,
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
                count: fetchState.count + event.items.length,
            };
        },
        requestedRepliesForId: (state, action: PayloadAction<string>) => {
            state.replies[action.payload] = {
                id: action.payload,
                status: "REQUESTED",
                count: 0,
            };
        },
        fetchedReplies: (state, action: PayloadAction<FetchCommentsEvent>) => {
            const event = action.payload;
            const fetchState = state.replies[event.objectId];
            state.replies[event.objectId] = {
                id: event.objectId,
                status: "FETCHING",
                count: fetchState.count + event.items.length,
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
                count: fetchState.count,
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

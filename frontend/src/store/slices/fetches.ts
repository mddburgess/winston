import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {VideoWithChannelIdDto} from "../../model/VideoDto";
import {FetchVideosEvent} from "../../model/events/FetchVideosEvent";
import {ChannelDto} from "../../model/ChannelDto";
import {CommentDto} from "../../model/CommentDto";

export type FetchState<T> = {
    id: string;
    mode?: 'ALL' | 'LATEST';
    status: 'READY' | 'REQUESTED' | 'FETCHING' | 'COMPLETED';
    data: T[];
}

type FetchStates = {
    channel: {
        [id: string]: FetchState<ChannelDto>;
    },
    videos: {
        [id: string]: FetchState<VideoWithChannelIdDto>;
    },
    comments: {
        [id: string]: FetchState<CommentDto>;
    }
}

const initialState: FetchStates = {
    channel: {},
    videos: {},
    comments: {},
}

type FetchVideosRequest = {
    channelId: string;
    mode: 'ALL' | 'LATEST';
}

export const fetchesSlice = createSlice({
    name: "fetches",
    initialState,
    reducers: {
        requestedChannelForHandle: (state, action: PayloadAction<string>) => {
            state.channel[action.payload] = {
                id: action.payload,
                status: 'REQUESTED',
                data: []
            }
        },
        initFetchStateForChannel: (state, action: PayloadAction<ChannelDto>) => {
            state.videos[action.payload.id] = {
                id: action.payload.id,
                status: 'READY',
                data: []
            }
        },
        requestedVideosForChannelId: (state, action: PayloadAction<FetchVideosRequest>) => {
            state.videos[action.payload.channelId] = {
                id: action.payload.channelId,
                mode: action.payload.mode,
                status: 'REQUESTED',
                data: []
            }
        },
        fetchedVideos: (state, action: PayloadAction<FetchVideosEvent>) => {
            const event = action.payload;
            const fetchState = state.videos[event.channelId];
            state.videos[event.channelId] = {
                id: event.channelId,
                status: event.status,
                data: (fetchState?.data ?? []).concat(event.videos)
            }
        },
        requestedCommentsForVideoId: (state, action: PayloadAction<string>) => {
            state.comments[action.payload] = {
                id: action.payload,
                status: 'REQUESTED',
                data: []
            }
        }
    }
})

export const {
    requestedChannelForHandle,
    initFetchStateForChannel,
    requestedVideosForChannelId,
    fetchedVideos,
    requestedCommentsForVideoId,
} = fetchesSlice.actions;

export default fetchesSlice.reducer;

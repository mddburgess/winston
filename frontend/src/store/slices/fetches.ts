import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {VideoWithChannelIdDto} from "../../model/VideoDto";
import {FetchVideosEvent} from "../../model/events/FetchVideosEvent";
import {ChannelDto} from "../../model/ChannelDto";

export type FetchState<T> = {
    id: string;
    status: 'READY' | 'REQUESTED' | 'FETCHING' | 'COMPLETED';
    data: T[];
}

type FetchStates = {
    channel: {
        [id: string]: FetchState<ChannelDto>;
    },
    videos: {
        [id: string]: FetchState<VideoWithChannelIdDto>;
    }
}

const initialState: FetchStates = {
    channel: {},
    videos: {},
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
        requestedVideosForChannelId: (state, action: PayloadAction<string>) => {
            state.videos[action.payload] = {
                id: action.payload,
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
        }
    }
})

export const {
    requestedChannelForHandle,
    initFetchStateForChannel,
    requestedVideosForChannelId,
    fetchedVideos,
} = fetchesSlice.actions;

export default fetchesSlice.reducer;

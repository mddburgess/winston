import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {VideoWithChannelIdDto} from "../../model/VideoDto";
import {FetchVideosEvent} from "../../model/events/FetchVideosEvent";
import {ChannelDto} from "../../model/ChannelDto";

export type FetchState = {
    id: string;
    status: 'READY' | 'REQUESTED' | 'FETCHING' | 'COMPLETED';
    videos: VideoWithChannelIdDto[];
}

type FetchStateMap = {
    [id: string]: FetchState;
}

const initialState: FetchStateMap = {}

export const fetchesSlice = createSlice({
    name: "fetches",
    initialState,
    reducers: {
        initFetchStateForChannel: (state, action: PayloadAction<ChannelDto>) => {
            state[action.payload.id] = {
                id: action.payload.id,
                status: 'READY',
                videos: []
            }
        },
        requestedVideosForChannelId: (state, action: PayloadAction<string>) => {
            state[action.payload] = {
                id: action.payload,
                status: 'REQUESTED',
                videos: []
            }
        },
        fetchedVideos: (state, action: PayloadAction<FetchVideosEvent>) => {
            const event = action.payload;
            const fetchState = state[event.channelId];
            state[event.channelId] = {
                id: event.channelId,
                status: event.status,
                videos: (fetchState?.videos ?? []).concat(event.videos)
            }
        }
    }
})

export const {
    initFetchStateForChannel,
    requestedVideosForChannelId,
    fetchedVideos,
} = fetchesSlice.actions;

export default fetchesSlice.reducer;

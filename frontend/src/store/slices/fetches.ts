import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {VideoWithChannelIdDto} from "../../model/VideoDto";
import {ChannelDto} from "../../model/ChannelDto";
import {CommentDto} from "../../model/CommentDto";
import {FetchCommentsEvent, FetchVideosEvent} from "../../model/events/FetchEvent";
import {ProblemDetail} from "../../model/events/ProblemDetail";

export type FetchState<T> = {
    id: string;
    mode?: 'ALL' | 'LATEST';
    status: 'READY' | 'REQUESTED' | 'FETCHING' | 'COMPLETED' | 'FAILED';
    data: T[];
    error?: ProblemDetail;
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
    },
    replies: {
        [id: string]: FetchState<CommentDto>;
    }
}

const initialState: FetchStates = {
    channel: {},
    videos: {},
    comments: {},
    replies: {},
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
            const fetchState = state.videos[event.objectId];
            state.videos[event.objectId] = {
                id: event.objectId,
                status: event.status,
                data: (fetchState?.data ?? []).concat(event.items ?? []),
                error: event.error,
            }
        },
        requestedCommentsForVideoId: (state, action: PayloadAction<string>) => {
            state.comments[action.payload] = {
                id: action.payload,
                status: 'REQUESTED',
                data: []
            }
        },
        fetchedComments: (state, action: PayloadAction<FetchCommentsEvent>) => {
            const event = action.payload;
            const fetchState = state.comments[event.objectId];
            state.comments[event.objectId] = {
                id: event.objectId,
                status: event.status,
                data: (fetchState?.data ?? []).concat(event.items ?? []),
                error: event.error,
            }
        },
        requestedRepliesForCommentId: (state, action: PayloadAction<string>) => {
            state.replies[action.payload] = {
                id: action.payload,
                status: 'REQUESTED',
                data: []
            }
        },
        fetchedReplies: (state, action: PayloadAction<FetchCommentsEvent>) => {
            const event = action.payload;
            const fetchState = state.replies[event.objectId];
            state.replies[event.objectId] = {
                id: event.objectId,
                status: event.status,
                data: (fetchState?.data ?? []).concat(event.items ?? []),
                error: event.error,
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
    fetchedComments,
    requestedRepliesForCommentId,
    fetchedReplies,
} = fetchesSlice.actions;

export default fetchesSlice.reducer;

import { createSlice } from "@reduxjs/toolkit";
import { xorBy } from "lodash";
import type { Video } from "#/api";
import type { PayloadAction } from "@reduxjs/toolkit";

type SelectionsState = {
    videos: Video[];
};

const initialState: SelectionsState = {
    videos: [],
};

const selectionsSlice = createSlice({
    name: "selections",
    initialState,
    reducers: {
        selectVideos: (state, action: PayloadAction<Video[]>) => {
            state.videos = action.payload;
        },
        toggleSelectVideo: (state, action: PayloadAction<Video>) => {
            state.videos = xorBy(state.videos, [action.payload], "id");
        },
    },
});

const selectionsReducer = selectionsSlice.reducer;
const { selectVideos, toggleSelectVideo } = selectionsSlice.actions;

export { selectionsReducer, selectVideos, toggleSelectVideo };

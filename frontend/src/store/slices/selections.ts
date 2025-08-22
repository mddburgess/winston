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
        toggleSelectVideo: (state, action: PayloadAction<Video>) => {
            state.videos = xorBy(state.videos, [action.payload], "id");
        },
        clearVideoSelection: (state) => {
            state.videos = [];
        },
    },
});

const selectionsReducer = selectionsSlice.reducer;
const { clearVideoSelection, toggleSelectVideo } = selectionsSlice.actions;

export { clearVideoSelection, selectionsReducer, toggleSelectVideo };

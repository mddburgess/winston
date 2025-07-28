import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

type SelectionsState = {
    videos: {
        [videoId: string]: boolean;
    };
};

const initialState: SelectionsState = {
    videos: {},
};

const selectionsSlice = createSlice({
    name: "selections",
    initialState,
    reducers: {
        selectVideos: (state, action: PayloadAction<string[]>) => {
            action.payload.forEach((videoId) => (state.videos[videoId] = true));
        },
        toggleSelectVideo: (state, action: PayloadAction<string>) => {
            const checked = state.videos[action.payload];
            state.videos[action.payload] = !checked;
        },
    },
});

const selections = selectionsSlice.reducer;
const { selectVideos, toggleSelectVideo } = selectionsSlice.actions;

export { selections, selectVideos, toggleSelectVideo };

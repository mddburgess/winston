import { createSlice } from "@reduxjs/toolkit";
import { xorBy } from "lodash";
import type { Video } from "#/api";
import type { PayloadAction } from "@reduxjs/toolkit";

type SelectionsState = {
  active: boolean;
  videos: Video[];
};

const initialState: SelectionsState = {
  active: false,
  videos: [],
};

const selectionsSlice = createSlice({
  name: "selections",
  initialState,
  reducers: {
    toggleVideoSelectionActive: (state) => {
      if (state.active) {
        state.videos = [];
      }
      state.active = !state.active;
    },
    toggleSelectVideo: (state, action: PayloadAction<Video>) => {
      state.videos = xorBy(state.videos, [action.payload], "id");
    },
    clearVideoSelection: (state) => {
      state.active = false;
      state.videos = [];
    },
  },
});

const selectionsReducer = selectionsSlice.reducer;
const { clearVideoSelection, toggleSelectVideo, toggleVideoSelectionActive } = selectionsSlice.actions;

export { clearVideoSelection, selectionsReducer, toggleSelectVideo, toggleVideoSelectionActive };

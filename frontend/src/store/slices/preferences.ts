import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

type PreferencesState = {
  showArchivedChannels: boolean;
  videosPageSize: number;
};

const initialState: PreferencesState = {
  showArchivedChannels: false,
  videosPageSize: 24,
};

const preferencesSlice = createSlice({
  name: "preferences",
  initialState,
  reducers: {
    setVideosPageSize: (state, { payload }: PayloadAction<number>) => {
      state.videosPageSize = payload;
    },
    toggleShowArchivedChannels: (state) => {
      state.showArchivedChannels = !state.showArchivedChannels;
    },
  },
});

const preferencesReducer = preferencesSlice.reducer;
const { setVideosPageSize, toggleShowArchivedChannels } = preferencesSlice.actions;

export { preferencesReducer, setVideosPageSize, toggleShowArchivedChannels };

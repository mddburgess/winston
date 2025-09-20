import { createSlice } from "@reduxjs/toolkit";

type PreferencesState = {
  showArchivedChannels: boolean;
};

const initialState: PreferencesState = {
  showArchivedChannels: false,
};

const preferencesSlice = createSlice({
  name: "preferences",
  initialState,
  reducers: {
    toggleShowArchivedChannels: (state) => {
      state.showArchivedChannels = !state.showArchivedChannels;
    },
  },
});

const preferencesReducer = preferencesSlice.reducer;
const { toggleShowArchivedChannels } = preferencesSlice.actions;

export { preferencesReducer, toggleShowArchivedChannels };

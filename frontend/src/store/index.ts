import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { enhancedBackendApi } from "#/store/slices/backend";
import { pullChannelsReducer } from "#/store/slices/pullChannels";
import { pullCommentsReducer } from "#/store/slices/pullComments";
import { pullRepliesReducer } from "#/store/slices/pullReplies";
import { pullVideosReducer } from "#/store/slices/pullVideos";
import { selectionsReducer } from "#/store/slices/selections";

const reducer = combineReducers({
  [enhancedBackendApi.reducerPath]: enhancedBackendApi.reducer,
  pullChannels: pullChannelsReducer,
  pullComments: pullCommentsReducer,
  pullReplies: pullRepliesReducer,
  pullVideos: pullVideosReducer,
  selections: selectionsReducer,
});

const setupStore = (preloadedState?: Partial<AppState>) => {
  return configureStore({
    reducer,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(enhancedBackendApi.middleware),
    preloadedState,
  });
};

type AppState = ReturnType<typeof reducer>;
type AppStore = ReturnType<typeof setupStore>;
type AppDispatch = AppStore["dispatch"];

export { setupStore };
export type { AppDispatch, AppState, AppStore };

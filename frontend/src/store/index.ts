import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { apiSlice } from "./slices/api";
import fetches from "./slices/fetches";

const reducer = combineReducers({
    [apiSlice.reducerPath]: apiSlice.reducer,
    fetches,
});

const setupStore = (preloadedState?: Partial<AppState>) => {
    return configureStore({
        reducer,
        middleware: (getDefaultMiddleware) =>
            getDefaultMiddleware().concat(apiSlice.middleware),
        preloadedState,
    });
};

type AppState = ReturnType<typeof reducer>;
type AppStore = ReturnType<typeof setupStore>;
type AppDispatch = AppStore["dispatch"];

export { setupStore };
export type { AppDispatch, AppState, AppStore };

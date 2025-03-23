import {configureStore} from "@reduxjs/toolkit";
import {apiSlice} from "./slices/api";
import fetches from "./slices/fetches";

export const store = configureStore({
    reducer: {
        [apiSlice.reducerPath]: apiSlice.reducer,
        fetches,
    },
    middleware: getDefaultMiddleware => getDefaultMiddleware()
        .concat(apiSlice.middleware)
});

export type AppStore = typeof store;
export type AppDispatch = typeof store.dispatch;
export type AppState = ReturnType<typeof store.getState>;

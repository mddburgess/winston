import {configureStore} from "@reduxjs/toolkit";
import {apiSlice} from "./slices/api";
import fetches from "./slices/fetches";
import notifications from "./slices/notifications";

export const store = configureStore({
    reducer: {
        [apiSlice.reducerPath]: apiSlice.reducer,
        fetches,
        notifications,
    },
    middleware: getDefaultMiddleware => getDefaultMiddleware()
        .concat(apiSlice.middleware)
});

export type AppStore = typeof store;
export type AppDispatch = typeof store.dispatch;
export type AppState = ReturnType<typeof store.getState>;

import {combineReducers, configureStore} from "@reduxjs/toolkit";
import {apiSlice} from "./slices/api";
import fetches from "./slices/fetches";

const reducer = combineReducers({
    [apiSlice.reducerPath]: apiSlice.reducer,
    fetches
})

export const setupStore = (preloadedState?: Partial<AppState>) => {
    return configureStore({
        reducer,
        middleware: getDefaultMiddleware => getDefaultMiddleware()
            .concat(apiSlice.middleware),
        preloadedState
    })
}

export type AppState = ReturnType<typeof reducer>;
export type AppStore = ReturnType<typeof setupStore>;
export type AppDispatch = AppStore['dispatch'];

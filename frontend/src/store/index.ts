import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { enhancedBackendApi } from "#/store/slices/backend";
import fetches from "#/store/slices/fetches";
import { selections } from "#/store/slices/selections";

const reducer = combineReducers({
    [enhancedBackendApi.reducerPath]: enhancedBackendApi.reducer,
    fetches,
    selections,
});

const setupStore = (preloadedState?: Partial<AppState>) => {
    return configureStore({
        reducer,
        middleware: (getDefaultMiddleware) =>
            getDefaultMiddleware().concat(enhancedBackendApi.middleware),
        preloadedState,
    });
};

type AppState = ReturnType<typeof reducer>;
type AppStore = ReturnType<typeof setupStore>;
type AppDispatch = AppStore["dispatch"];

export { setupStore };
export type { AppDispatch, AppState, AppStore };

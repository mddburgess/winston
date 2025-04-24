import {
    render,
    renderHook,
    RenderHookOptions,
    RenderOptions,
} from "@testing-library/react";
import { AppState, AppStore, setupStore } from "../store";
import { PropsWithChildren, ReactElement } from "react";
import { Provider } from "react-redux";

type ExtendedRenderOptions = Omit<RenderOptions, "queries"> & {
    preloadedState?: Partial<AppState>;
    store?: AppStore;
};

export const renderWithProviders = (
    ui: ReactElement,
    options: ExtendedRenderOptions = {},
) => {
    const {
        preloadedState = {},
        store = setupStore(preloadedState),
        ...renderOptions
    } = options;

    const wrapper = ({ children }: PropsWithChildren) => (
        <Provider store={store}>{children}</Provider>
    );

    return {
        store,
        ...render(ui, { wrapper, ...renderOptions }),
    };
};

type ExtendedRenderHookOptions<Props> = Omit<
    RenderHookOptions<Props>,
    "queries"
> & {
    preloadedState?: Partial<AppState>;
    store?: AppStore;
};

export const renderHookWithProviders = <Props, Result>(
    hook: (initialProps?: Props) => Result,
    options: ExtendedRenderHookOptions<Props> = {},
) => {
    const {
        preloadedState = {},
        store = setupStore(preloadedState),
        ...renderHookOptions
    } = options;

    const wrapper = ({ children }: PropsWithChildren) => (
        <Provider store={store}>{children}</Provider>
    );

    return {
        store,
        ...renderHook(hook, { wrapper, ...renderHookOptions }),
    };
};

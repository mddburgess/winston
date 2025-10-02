import { render, renderHook } from "@testing-library/react";
import { userEvent } from "@testing-library/user-event";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router";
import { setupStore } from "#/store";
import type { AppState, AppStore } from "#/store";
import type { RenderHookOptions, RenderOptions } from "@testing-library/react";
import type { PropsWithChildren, ReactElement } from "react";

type ExtendedRenderOptions = Omit<RenderOptions, "queries"> & {
  preloadedState?: Partial<AppState>;
  store?: AppStore;
};

const renderWithProviders = (ui: ReactElement, options: ExtendedRenderOptions = {}) => {
  const { preloadedState = {}, store = setupStore(preloadedState), ...renderOptions } = options;

  const wrapper = ({ children }: PropsWithChildren) => <Provider store={store}>{children}</Provider>;

  return {
    store,
    ...render(ui, { wrapper, ...renderOptions }),
  };
};

type ExtendedRenderHookOptions<Props> = Omit<RenderHookOptions<Props>, "queries"> & {
  preloadedState?: Partial<AppState>;
  store?: AppStore;
};

const renderHookWithProviders = <Props, Result>(
  hook: (initialProps?: Props) => Result,
  options: ExtendedRenderHookOptions<Props> = {},
) => {
  const { preloadedState = {}, store = setupStore(preloadedState), ...renderHookOptions } = options;

  const wrapper = ({ children }: PropsWithChildren) => <Provider store={store}>{children}</Provider>;

  return {
    store,
    ...renderHook(hook, { wrapper, ...renderHookOptions }),
  };
};

const renderWithRouter = (ui: ReactElement, { route = "/" } = {}) => {
  window.history.pushState({}, "", route);

  return {
    user: userEvent.setup(),
    ...render(ui, { wrapper: BrowserRouter }),
  };
};

export { renderHookWithProviders, renderWithProviders, renderWithRouter };

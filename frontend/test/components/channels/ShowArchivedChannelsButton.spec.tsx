import { userEvent } from "@testing-library/user-event";
import { describe, expect, it } from "vitest";
import { ShowArchivedChannelsButton } from "#/components/channels/ShowArchivedChannelsButton";
import { renderWithProviders } from "=/utils/render";

describe(ShowArchivedChannelsButton, () => {
  const user = userEvent.setup();

  it("displays a button to toggle showing archived channels", () => {
    const result = renderWithProviders(<ShowArchivedChannelsButton />);
    const button = result.getByRole("button");
    const buttonIcon = result.getByTestId("icon");

    expect(button).toHaveClass("btn-outline-secondary");
    expect(buttonIcon).toHaveClass("bi-archive");
  });

  it.todo("displays a tooltip when hovering over the button", async () => {
    const result = renderWithProviders(<ShowArchivedChannelsButton />);
    const button = result.getByRole("button");
    await user.hover(button);
    const tooltip = result.getByRole("tooltip");

    expect(tooltip).toHaveTextContent("Show archived");

    await user.unhover(button);

    expect(tooltip).not.toBeInTheDocument();
  });

  it("displays a filled-in button when the show archived channels preference is true", () => {
    const result = renderWithProviders(<ShowArchivedChannelsButton />, {
      preloadedState: { preferences: { showArchivedChannels: true, videosPageSize: 24 } },
    });
    const button = result.getByRole("button");
    const buttonIcon = result.getByTestId("icon");

    expect(button).toHaveClass("btn-secondary");
    expect(buttonIcon).toHaveClass("bi-archive-fill");
  });

  it("toggles the show archived channels preference when clicked", async () => {
    const result = renderWithProviders(<ShowArchivedChannelsButton />);
    const button = result.getByRole("button");
    await user.click(button);

    expect(result.store.getState().preferences.showArchivedChannels).toBe(true);

    await user.click(button);

    expect(result.store.getState().preferences.showArchivedChannels).toBe(false);
  });
});

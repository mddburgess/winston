import { userEvent } from "@testing-library/user-event";
import { RefreshChannelButton } from "#/components/channels/RefreshChannelButton";
import { backend } from "=/mocks/backend";
import { pullChannelsEvent, pullOperationEvent } from "=/mocks/data/events";
import { mockChannel } from "=/mocks/data/mockChannel";
import { eventsHandler } from "=/mocks/handlers/eventsHandler";
import { renderWithProviders } from "=/utils/render";

describe(RefreshChannelButton, () => {
  const user = userEvent.setup();

  it("displays an icon to refresh the channel", () => {
    const channel = mockChannel();
    const result = renderWithProviders(<RefreshChannelButton channel={channel} />);
    const refreshChannelIcon = result.getByTestId("refreshChannelIcon");

    expect(refreshChannelIcon).toHaveClass("bi-arrow-clockwise");
  });

  it("refreshes channel data when the icon is clicked", async () => {
    const channel = mockChannel();
    const result = renderWithProviders(<RefreshChannelButton channel={channel} />);
    const refreshChannelIcon = result.getByTestId("refreshChannelIcon");

    backend.use(...eventsHandler(pullChannelsEvent(channel), pullOperationEvent()));
    await user.click(refreshChannelIcon);

    expect(refreshChannelIcon).not.toBeInTheDocument();

    const refreshChannelSuccessIcon = result.getByTestId("refreshChannelSuccessIcon");

    expect(refreshChannelSuccessIcon).toHaveClass("bi-check-circle-fill text-success");
  });
});

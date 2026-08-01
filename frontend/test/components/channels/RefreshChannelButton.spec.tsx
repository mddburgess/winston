import { userEvent } from "@testing-library/user-event";
import { RefreshChannelButton } from "#/components/channels/RefreshChannelButton";
import { backend } from "=/mocks/backend";
import { apiErrors } from "=/mocks/data/apiErrors";
import { pullChannelsEvent, pullOperationEvent } from "=/mocks/data/events";
import { mockChannel } from "=/mocks/data/mockChannel";
import { eventsHandler } from "=/mocks/handlers/eventsHandler";
import { renderWithProviders } from "=/utils/render";

describe(RefreshChannelButton, () => {
  const user = userEvent.setup();
  const channel = mockChannel();

  it("displays an icon to refresh the channel", () => {
    const result = renderWithProviders(<RefreshChannelButton channel={channel} />);
    const refreshChannelIcon = result.getByTestId("refreshChannelIcon");

    expect(refreshChannelIcon).toHaveClass("bi-arrow-clockwise");
  });

  it.todo("refreshes channel data when the icon is clicked", async () => {
    backend.use(...eventsHandler(pullChannelsEvent(channel), pullOperationEvent({ channelHandle: channel.handle })));

    const result = renderWithProviders(<RefreshChannelButton channel={channel} />);
    const refreshChannelIcon = result.getByTestId("refreshChannelIcon");
    await user.click(refreshChannelIcon);

    expect(refreshChannelIcon).not.toBeInTheDocument();

    const refreshChannelSuccessIcon = result.getByTestId("refreshChannelSuccessIcon");

    expect(refreshChannelSuccessIcon).toHaveClass("bi-check-circle-fill text-success");
  });

  it.todo("displays an error icon when the refresh request fails", async () => {
    backend.use(
      ...eventsHandler(pullOperationEvent({ channelHandle: channel.handle, error: apiErrors.channelNotFound })),
    );

    const result = renderWithProviders(<RefreshChannelButton channel={channel} />);
    const refreshChannelIcon = result.getByTestId("refreshChannelIcon");
    await user.click(refreshChannelIcon);

    expect(refreshChannelIcon).not.toBeInTheDocument();

    const refreshChannelFailedIcon = result.getByTestId("refreshChannelFailedIcon");

    expect(refreshChannelFailedIcon).toHaveClass("bi-x-octagon-fill text-danger");
  });
});

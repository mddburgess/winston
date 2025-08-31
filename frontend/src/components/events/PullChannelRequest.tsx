import { usePullMutation } from "#/api";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { appendChannels } from "#/store/slices/channels";
import { pullChannelActive, pullChannelError, pullChannelResponse } from "#/store/slices/pullChannel";
import type { PullChannelsEvent, PullOperationEvent } from "#/components/events/types";

const PullChannelRequest = () => {
  const dispatch = useAppDispatch();
  const { active, requested = "" } = useAppSelector((state) => state.pullChannel);

  const [pull] = usePullMutation();

  const requestPullChannel = (eventSubscriptionId: string) => {
    void pull({
      body: {
        event_subscription_id: eventSubscriptionId,
        operations: [{ pull: "channel", channel_handle: requested }],
      },
    });
  };

  const handlePullOperationEvent = (event: PullOperationEvent) => {
    dispatch(pullChannelResponse({ channelHandle: requested, status: event.operation.status }));
    if (event.error) {
      dispatch(pullChannelError({ channelHandle: requested, error: event.error }));
    }
  };

  const handlePullChannelsEvent = (event: PullChannelsEvent) => {
    if (event.channels.length > 0) {
      dispatch(appendChannels(event.channels));
    }
  };

  const handleUnsubscribed = () => {
    dispatch(pullChannelActive(false));
  };

  return (
    active && (
      <PullEventsSource
        whenSubscribed={requestPullChannel}
        onPullOperationEvent={handlePullOperationEvent}
        onPullChannelsEvent={handlePullChannelsEvent}
        whenUnsubscribed={handleUnsubscribed}
      />
    )
  );
};

export { PullChannelRequest };

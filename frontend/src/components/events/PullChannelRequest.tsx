import { usePullMutation } from "#/api";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { useAppDispatch, useAppSelector } from "#/store/hooks";
import { invalidateChannels } from "#/store/slices/backend";
import { pullChannelActive, pullChannelError, pullChannelResponse } from "#/store/slices/pullChannel";
import type { PullChannelsEvent, PullOperationEvent } from "#/components/events/types";
import type { PullOperation } from "#/types";

const PullChannelRequest = () => {
  const dispatch = useAppDispatch();
  const { active, requested } = useAppSelector((state) => state.pullChannel);

  const [pull] = usePullMutation();

  const requestPullChannel = (eventSubscriptionId: string) => {
    const operations: PullOperation[] = requested.map((handle) => ({ pull: "channel", channel_handle: handle }));
    void pull({ body: { event_subscription_id: eventSubscriptionId, operations } });
  };

  const handlePullOperationEvent = (event: PullOperationEvent) => {
    if (event.operation.pull === "channel") {
      const channelHandle = event.operation.channel_handle;
      dispatch(pullChannelResponse({ channelHandle, status: event.operation.status }));
      if (event.error) {
        dispatch(pullChannelError({ channelHandle, error: event.error }));
      }
    }
  };

  const handlePullChannelsEvent = (event: PullChannelsEvent) => {
    if (event.channels.length > 0) {
      dispatch(invalidateChannels());
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

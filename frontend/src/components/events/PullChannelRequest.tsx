import { useState } from "react";
import { useNavigate } from "react-router";
import { EventSourceProvider } from "react-sse-hooks";
import { usePullMutation } from "#/api";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { useAppDispatch } from "#/store/hooks";
import { appendChannels } from "#/store/slices/channels";
import { routes } from "#/utils/links";
import type { Channel, Problem } from "#/api";
import type { PullChannelsEvent, PullRequestEvent } from "#/components/events/types";

const PullChannelRequest = (props: {
  channelHandle: string;
  onSuccess?: (channel: Channel) => void;
  onError?: (error: Problem) => void;
}) => {
  const dispatch = useAppDispatch();
  const [pull] = usePullMutation();

  const navigate = useNavigate();
  const [channel, setChannel] = useState<Channel>();

  const requestPullChannel = (eventSubscriptionId: string) => {
    void pull({
      body: {
        event_subscription_id: eventSubscriptionId,
        operations: [{ pull: "channel", channel_handle: props.channelHandle }],
      },
    });
  };

  const handlePullRequestEvent = (event: PullRequestEvent) => {
    if (event.request.status === "completed" && channel) {
      void navigate(routes.channels.details(channel.handle));
    }
  };

  const handlePullChannelsEvent = (event: PullChannelsEvent) => {
    if (event.channels.length > 0) {
      dispatch(appendChannels(event.channels));
      setChannel(event.channels[0]);
    }
  };

  return (
    <EventSourceProvider>
      <PullEventsSource
        whenSubscribed={requestPullChannel}
        onPullRequestEvent={handlePullRequestEvent}
        onPullChannelsEvent={handlePullChannelsEvent}
      />
    </EventSourceProvider>
  );
};

export { PullChannelRequest };

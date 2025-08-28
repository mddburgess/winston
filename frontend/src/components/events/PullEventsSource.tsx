import { useEffect } from "react";
import { useEventSource, useEventSourceListener } from "react-sse-hooks";
import type {
  EventSubscriptionEvent,
  PullChannelsEvent,
  PullCommentsEvent,
  PullOperationEvent,
  PullRepliesEvent,
  PullRequestEvent,
  PullVideosEvent,
} from "#/components/events/types";

type AppEventsSourceProps = {
  whenSubscribed?: (subscriptionId: string) => void;
  onPullRequestEvent?: (pullRequestEvent: PullRequestEvent) => void;
  onPullOperationEvent?: (pullOperationEvent: PullOperationEvent) => void;
  onPullChannelsEvent?: (pullChannelsEvent: PullChannelsEvent) => void;
  onPullVideosEvent?: (pullVideosEvent: PullVideosEvent) => void;
  onPullCommentsEvent?: (pullCommentsEvent: PullCommentsEvent) => void;
  onPullRepliesEvent?: (pullRepliesEvent: PullRepliesEvent) => void;
};

const PullEventsSource = ({
  whenSubscribed = () => {},
  onPullRequestEvent = () => {},
  onPullOperationEvent = () => {},
  onPullChannelsEvent = () => {},
  onPullVideosEvent = () => {},
  onPullCommentsEvent = () => {},
  onPullRepliesEvent = () => {},
}: AppEventsSourceProps) => {
  const eventSource = useEventSource({ source: `/api/v1/notifications` });

  useEffect(() => {
    eventSource.onerror = (event) => {
      console.debug("Event source closed:", event);
      eventSource.close();
    };
  }, [eventSource]);

  useEventSourceListener<EventSubscriptionEvent>(
    {
      source: eventSource,
      startOnInit: true,
      event: {
        name: "event-subscription",
        listener: (event) => {
          if (event.data.subscribed) {
            console.debug("Subscribed to app events:", event.data);
            whenSubscribed(event.data.event_subscription_id);
          } else {
            console.debug("Unsubscribed from app events:", event.data);
          }
        },
      },
    },
    [eventSource, whenSubscribed],
  );

  const handleEvent = <T,>(name: string, handler: (event: T) => void) => ({
    source: eventSource,
    startOnInit: true,
    event: {
      name: name,
      listener: (event: { data: T }) => {
        console.debug(`Received pull event of type '${name}':`, event.data);
        handler(event.data);
      },
    },
  });

  useEventSourceListener<PullRequestEvent>(handleEvent("pull-request", onPullRequestEvent), [
    eventSource,
    onPullRequestEvent,
  ]);
  useEventSourceListener<PullOperationEvent>(handleEvent("pull-operation", onPullOperationEvent), [
    eventSource,
    onPullOperationEvent,
  ]);
  useEventSourceListener<PullChannelsEvent>(handleEvent("pull-channels", onPullChannelsEvent), [
    eventSource,
    onPullChannelsEvent,
  ]);
  useEventSourceListener<PullVideosEvent>(handleEvent("pull-videos", onPullVideosEvent), [
    eventSource,
    onPullVideosEvent,
  ]);
  useEventSourceListener<PullCommentsEvent>(handleEvent("pull-comments", onPullCommentsEvent), [
    eventSource,
    onPullCommentsEvent,
  ]);
  useEventSourceListener<PullRepliesEvent>(handleEvent("pull-replies", onPullRepliesEvent), [
    eventSource,
    onPullRepliesEvent,
  ]);

  return <></>;
};

export { PullEventsSource };

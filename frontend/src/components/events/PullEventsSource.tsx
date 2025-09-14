import { EventSource } from "eventsource";
import { useEffect } from "react";
import { EventSourceProvider, useEventSource, useEventSourceListener } from "react-sse-hooks";
import type {
  EventSubscriptionEvent,
  PullOperationEvent,
  PullRequestEvent,
  PullResultsEvent,
} from "#/components/events/types";

type PullEventsSourceProps<T> = {
  whenSubscribed?: (subscriptionId: string) => void;
  onPullRequestEvent?: (pullRequestEvent: PullRequestEvent) => void;
  onPullOperationEvent?: (pullOperationEvent: PullOperationEvent) => void;
  onPullResultsEvent?: (pullResultsEvent: PullResultsEvent<T>) => void;
  whenUnsubscribed?: () => void;
};

const PullEventsSource = <T,>(props: PullEventsSourceProps<T>) => (
  <EventSourceProvider eventSource={EventSource}>
    <PullEventsSourceInternal {...props} />
  </EventSourceProvider>
);

const PullEventsSourceInternal = <T,>({
  whenSubscribed = () => {},
  onPullRequestEvent = () => {},
  onPullOperationEvent = () => {},
  onPullResultsEvent = () => {},
  whenUnsubscribed = () => {},
}: PullEventsSourceProps<T>) => {
  const eventSource = useEventSource({ source: `/api/v1/notifications` });

  useEffect(() => {
    eventSource.onerror = (event) => {
      console.debug("Event source closed:", event);
      eventSource.close();
      whenUnsubscribed();
    };
  }, [eventSource, whenUnsubscribed]);

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
            whenUnsubscribed();
            eventSource.close();
          }
        },
      },
    },
    [eventSource, whenSubscribed, whenUnsubscribed],
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
  useEventSourceListener<PullResultsEvent<T>>(handleEvent("pull-results", onPullResultsEvent), [
    eventSource,
    onPullResultsEvent,
  ]);

  return <></>;
};

export { PullEventsSource };

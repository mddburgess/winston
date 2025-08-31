import { render } from "@testing-library/react";
import { EventSource } from "eventsource";
import { EventSourceProvider } from "react-sse-hooks";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { backend } from "?/mocks/backend";
import {
  eventSubscriptionEvent,
  pullChannelsEvent,
  pullCommentsEvent,
  pullOperationEvent,
  pullRepliesEvent,
  pullRequestEvent,
  pullVideosEvent,
} from "?/mocks/data/events";
import { eventsHandler } from "?/mocks/handlers/events";

describe(PullEventsSource, () => {
  const callback = vi.fn();

  beforeEach(() => {
    callback.mockReset();
  });

  describe("whenSubscribed callback", () => {
    it("is called when an event-subscription event is received with subscribed = true", async () => {
      backend.use(eventsHandler(eventSubscriptionEvent(true)));
      render(
        <EventSourceProvider eventSource={EventSource}>
          <PullEventsSource whenSubscribed={callback} />
        </EventSourceProvider>,
      );

      await expect.poll(() => callback).toHaveBeenCalled();
    });

    it("is not called when an event-subscription event is received with subscribed = false", async () => {
      backend.use(eventsHandler(eventSubscriptionEvent(false)));
      const whenUnsubscribed = vi.fn();
      render(
        <EventSourceProvider eventSource={EventSource}>
          <PullEventsSource whenSubscribed={callback} whenUnsubscribed={whenUnsubscribed} />
        </EventSourceProvider>,
      );

      await expect.poll(() => whenUnsubscribed).toHaveBeenCalled();
      expect(callback).not.toHaveBeenCalled();
    });
  });

  describe("onPullRequest callback", () => {
    it("is called when a pull-request event is received", async () => {
      backend.use(eventsHandler(pullRequestEvent()));
      render(
        <EventSourceProvider eventSource={EventSource}>
          <PullEventsSource onPullRequestEvent={callback} />
        </EventSourceProvider>,
      );

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });

  describe("onPullOperation callback", () => {
    it("is called when a pull-operation event is received", async () => {
      backend.use(eventsHandler(pullOperationEvent()));
      render(
        <EventSourceProvider eventSource={EventSource}>
          <PullEventsSource onPullOperationEvent={callback} />
        </EventSourceProvider>,
      );

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });

  describe("onPullChannelsEvent callback", () => {
    it("is called when a pull-channels event is received", async () => {
      backend.use(eventsHandler(pullChannelsEvent()));
      render(
        <EventSourceProvider eventSource={EventSource}>
          <PullEventsSource onPullChannelsEvent={callback} />
        </EventSourceProvider>,
      );

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });

  describe("onPullVideosEvent callback", () => {
    it("is called when a pull-videos event is received", async () => {
      backend.use(eventsHandler(pullVideosEvent()));
      render(
        <EventSourceProvider eventSource={EventSource}>
          <PullEventsSource onPullVideosEvent={callback} />
        </EventSourceProvider>,
      );

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });

  describe("onPullCommentsEvent callback", () => {
    it("is called when a pull-comments event is received", async () => {
      backend.use(eventsHandler(pullCommentsEvent()));
      render(
        <EventSourceProvider eventSource={EventSource}>
          <PullEventsSource onPullCommentsEvent={callback} />
        </EventSourceProvider>,
      );

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });

  describe("onPullRepliesEvent callback", () => {
    it("is called when a pull-replies event is received", async () => {
      backend.use(eventsHandler(pullRepliesEvent()));
      render(
        <EventSourceProvider eventSource={EventSource}>
          <PullEventsSource onPullRepliesEvent={callback} />
        </EventSourceProvider>,
      );

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });
});

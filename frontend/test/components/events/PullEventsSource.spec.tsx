import { faker } from "@faker-js/faker";
import { render } from "@testing-library/react";
import { PullEventsSource } from "#/components/events/PullEventsSource";
import { backend } from "=/mocks/backend";
import {
  eventSubscriptionEvent,
  pullChannelsEvent,
  pullCommentsEvent,
  pullOperationEvent,
  pullRepliesEvent,
  pullRequestEvent,
  pullVideosEvent,
} from "=/mocks/data/events";
import { simpleEventsHandler } from "=/mocks/handlers/simpleEventsHandler";

describe(PullEventsSource, () => {
  const callback = vi.fn();

  beforeEach(() => {
    callback.mockReset();
  });

  describe("whenSubscribed callback", () => {
    const eventSubscriptionId = faker.string.uuid();

    it("is called when an event-subscription event is received with subscribed = true", async () => {
      backend.use(simpleEventsHandler(eventSubscriptionEvent(eventSubscriptionId, true)));
      render(<PullEventsSource whenSubscribed={callback} />);

      await expect.poll(() => callback).toHaveBeenCalled();
    });

    it("is not called when an event-subscription event is received with subscribed = false", async () => {
      backend.use(simpleEventsHandler(eventSubscriptionEvent(eventSubscriptionId, false)));
      const whenUnsubscribed = vi.fn();
      render(<PullEventsSource whenSubscribed={callback} whenUnsubscribed={whenUnsubscribed} />);

      await expect.poll(() => whenUnsubscribed).toHaveBeenCalled();
      expect(callback).not.toHaveBeenCalled();
    });
  });

  describe("onPullRequest callback", () => {
    it("is called when a pull-request event is received", async () => {
      backend.use(simpleEventsHandler(pullRequestEvent()));
      render(<PullEventsSource onPullRequestEvent={callback} />);

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });

  describe("onPullOperation callback", () => {
    it("is called when a pull-operation event is received", async () => {
      backend.use(simpleEventsHandler(pullOperationEvent()));
      render(<PullEventsSource onPullOperationEvent={callback} />);

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });

  describe("onPullChannelsEvent callback", () => {
    it("is called when a pull-channels event is received", async () => {
      backend.use(simpleEventsHandler(pullChannelsEvent()));
      render(<PullEventsSource onPullChannelsEvent={callback} />);

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });

  describe("onPullVideosEvent callback", () => {
    it("is called when a pull-videos event is received", async () => {
      backend.use(simpleEventsHandler(pullVideosEvent()));
      render(<PullEventsSource onPullVideosEvent={callback} />);

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });

  describe("onPullCommentsEvent callback", () => {
    it("is called when a pull-comments event is received", async () => {
      backend.use(simpleEventsHandler(pullCommentsEvent()));
      render(<PullEventsSource onPullCommentsEvent={callback} />);

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });

  describe("onPullRepliesEvent callback", () => {
    it("is called when a pull-replies event is received", async () => {
      backend.use(simpleEventsHandler(pullRepliesEvent()));
      render(<PullEventsSource onPullRepliesEvent={callback} />);

      await expect.poll(() => callback).toHaveBeenCalled();
    });
  });
});

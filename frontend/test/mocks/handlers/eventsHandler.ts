import { faker } from "@faker-js/faker";
import { http, HttpResponse } from "msw";
import { eventSubscriptionEvent } from "=/mocks/data/events";
import { encodeEvent } from "=/mocks/handlers/encodeEvent";
import type { AppEvent } from "#/components/events/types";

/**
 * Builds a set of MSW handlers for the event notifications and pull endpoints.
 *
 * The handlers replicate the behaviour of the backend notifications and pull endpoints:
 * 1. The notifications handler returns an event stream and immediately sends the opening event-subscription event.
 * 2. The pull handler then sends the specified events followed by the closing event-subscription event,
 *    and closes the stream.
 *
 * @param events the list of app events (other than event-subscription events) that the handlers should respond with
 *               when the mock event notifications and pull endpoints are called, listed in the order that the events
 *               should be returned in the event stream
 * @returns an array containing the MSW handlers
 */
const eventsHandler = (...events: AppEvent[]) => {
  const eventSubscriptionId = faker.string.uuid();
  let streamController: ReadableStreamDefaultController;

  const getNotifications = http.get("/api/v1/notifications", () => {
    const stream = new ReadableStream({
      start: (controller) => {
        controller.enqueue(encodeEvent(eventSubscriptionEvent(eventSubscriptionId, true)));
        streamController = controller;
      },
    });
    return new HttpResponse(stream, { headers: { "Content-Type": "text/event-stream" } });
  });

  const postPull = http.post("/api/v1/pull", () => {
    events.forEach((event) => {
      streamController.enqueue(encodeEvent(event));
    });
    streamController.enqueue(encodeEvent(eventSubscriptionEvent(eventSubscriptionId, false)));
    streamController.close();
    return new HttpResponse(null, { status: 202 });
  });

  return [getNotifications, postPull];
};

export { eventsHandler };

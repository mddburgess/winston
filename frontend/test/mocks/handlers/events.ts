import { http, HttpResponse } from "msw";
import type { AppEvent } from "#/components/events/types";

const encoder = new TextEncoder();

/**
 * Builds the MSW handler for the event notifications endpoint.
 *
 * @param events the list of app events that the handler should respond with when the mock event notifications endpoint
 *               is called, listed in the order that the events should be returned in the event stream
 * @returns the MSW handler
 */
const eventsHandler = (...events: AppEvent[]) =>
  http.get("/api/v1/notifications", () => {
    const stream = new ReadableStream({
      start: (controller) => {
        events.forEach((event) => controller.enqueue(encodeEvent(event)));
        controller.close();
      },
    });
    return new HttpResponse(stream, { headers: { "Content-Type": "text/event-stream" } });
  });

/**
 * Encodes an app event as a server-sent event message.
 *
 * @param event the app event to encode
 * @returns the event encoded as a server-sent event message
 */
const encodeEvent = (event: AppEvent) =>
  encoder.encode(
    [`id: ${event.event_id}`, `event: ${event.event_type}`, `data: ${JSON.stringify(event)}`, "\n"].join("\n"),
  );

export { eventsHandler };

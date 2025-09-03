import { http, HttpResponse } from "msw";
import { encodeEvent } from "=/mocks/handlers/encodeEvent";
import type { AppEvent } from "#/components/events/types";

/**
 * Builds a simple MSW handler for the event notifications endpoint.
 *
 * @param events the list of app events that the handler should respond with when the mock event notifications endpoint
 *               is called, listed in the order that the events should be returned in the event stream
 * @returns a simple MSW handler
 */
const simpleEventsHandler = (...events: AppEvent[]) =>
  http.get("/api/v1/notifications", () => {
    const stream = new ReadableStream({
      start: (controller) => {
        events.forEach((event) => {
          controller.enqueue(encodeEvent(event));
        });
        controller.close();
      },
    });
    return new HttpResponse(stream, { headers: { "Content-Type": "text/event-stream" } });
  });

export { simpleEventsHandler };

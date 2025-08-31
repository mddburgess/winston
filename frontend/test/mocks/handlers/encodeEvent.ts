import type { AppEvent } from "#/components/events/types";

const encoder = new TextEncoder();

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

export { encodeEvent };

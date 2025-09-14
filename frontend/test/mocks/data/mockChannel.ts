import { faker } from "@faker-js/faker";
import { DateTime } from "luxon";
import type { Channel } from "#/api";

/**
 * Creates a mock channel for testing.
 *
 * Data passed in the `partial` parameter will be set on the mock channel.
 * All other properties will be set to random values.
 *
 * @param partial channel data to set in the mock channel
 * @returns the mock channel
 */
const mockChannel = (partial: Partial<Channel> = {}): Channel => ({
  id: faker.string.nanoid(),
  title: faker.string.alpha(),
  description: faker.string.alpha(),
  handle: `@${faker.internet.username()}`,
  thumbnail_url: faker.internet.url(),
  statistics: {
    video_count: faker.number.int({ min: 0, max: 10_000 }),
    view_count: faker.number.int({ min: 0, max: 10_000_000 }),
    subscriber_count: faker.number.int({ min: 0, max: 10_000_000 }),
  },
  topics: [],
  keywords: [],
  video_count: faker.number.int({ min: 0, max: 10_000 }),
  published_at: DateTime.fromJSDate(faker.date.recent()).toISO() ?? "",
  last_fetched_at: DateTime.fromJSDate(faker.date.recent()).toISO() ?? "",
  ...partial,
});

export { mockChannel };

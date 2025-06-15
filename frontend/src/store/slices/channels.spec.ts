import { createEntityAdapter } from "@reduxjs/toolkit";
import { waitFor } from "@testing-library/react";
import { http, HttpResponse } from "msw";
import { backend } from "#/mocks/backend";
import { renderHookWithProviders } from "#/utils/test-utils";
import { appendChannels, useListChannelsQuery } from "./channels.ts";
import type { Channel } from "#/api";

describe("channelsApi", () => {
    const entityAdapter = createEntityAdapter<Channel>();

    describe("listChannels", () => {
        it("handles a 200 response with a list of channels", async () => {
            const { result } = renderHookWithProviders(() =>
                useListChannelsQuery(),
            );
            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toBeDefined();

            const data = result.current.data!;
            const channels = entityAdapter.getSelectors().selectAll(data);

            expect(channels).toStrictEqual([
                {
                    id: "channel.1",
                    title: "channel.1.title",
                    description: "channel.1.description",
                    customUrl: "@channel1url",
                    thumbnailUrl: "/api/v1/channels/1/thumbnail",
                    topics: ["https://en.wikipedia.org/wiki/Topic"],
                    keywords: ["keyword"],
                    videoCount: 1,
                    publishedAt: "2025-01-01T00:00:00Z",
                    lastFetchedAt: "2025-01-02T00:00:00.000000Z",
                },
            ]);
        });

        it("handles a 200 response with an empty list", async () => {
            backend.use(
                http.get("/api/v1/channels", () => {
                    return HttpResponse.json({
                        channels: [],
                    });
                }),
            );
            const { result } = renderHookWithProviders(() =>
                useListChannelsQuery(),
            );
            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toBeDefined();

            const data = result.current.data!;
            const channels = entityAdapter.getSelectors().selectAll(data);

            expect(channels).toStrictEqual([]);
        });
    });

    describe(appendChannels, () => {
        it("adds a list of channels to the listChannels cache", async () => {
            const { store, result } = renderHookWithProviders(() =>
                useListChannelsQuery(),
            );
            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            store.dispatch(
                appendChannels([
                    {
                        id: "channel.2",
                        title: "channel.2.title",
                        description: "channel.2.description",
                        handle: "@channel2url",
                        thumbnail_url: "/api/v1/channels/2/thumbnail",
                        topics: ["https://en.wikipedia.org/wiki/Topic"],
                        keywords: ["keyword"],
                        video_count: 1,
                        published_at: "2025-02-01T00:00:00Z",
                        last_fetched_at: "2025-02-02T00:00:00.000000Z",
                    },
                ]),
            );
            const { result: updated } = renderHookWithProviders(
                () => useListChannelsQuery(),
                { store },
            );

            expect(updated.current.data).toBeDefined();

            const data = updated.current.data!;
            const channels = entityAdapter.getSelectors().selectAll(data);

            expect(channels).toHaveLength(2);
            expect(channels[0].id).toBe("channel.1");
            expect(channels[1].id).toBe("channel.2");
        });
    });
});

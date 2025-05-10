import { appendFetchedChannels, useListChannelsQuery } from "./channels";
import { waitFor } from "@testing-library/react";
import { createEntityAdapter } from "@reduxjs/toolkit";
import type { ChannelDto } from "../../model/ChannelDto";
import { http, HttpResponse } from "msw";
import { backend } from "../../mocks/backend";
import { renderHookWithProviders } from "../../utils/test-utils";

describe("channelsApi", () => {
    const entityAdapter = createEntityAdapter<ChannelDto>();

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
                    return HttpResponse.json([]);
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

    describe("appendFetchedChannels", () => {
        it("updates the listChannels cache with the newly fetched list of channels", async () => {
            const { store, result } = renderHookWithProviders(() =>
                useListChannelsQuery(),
            );
            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            store.dispatch(
                appendFetchedChannels([
                    {
                        id: "channel.2",
                        title: "channel.2.title",
                        description: "channel.2.description",
                        customUrl: "@channel2url",
                        thumbnailUrl: "/api/v1/channels/2/thumbnail",
                        topics: ["https://en.wikipedia.org/wiki/Topic"],
                        keywords: ["keyword"],
                        videoCount: 1,
                        publishedAt: "2025-02-01T00:00:00Z",
                        lastFetchedAt: "2025-02-02T00:00:00.000000Z",
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
            expect(channels[0].id).toEqual("channel.1");
            expect(channels[1].id).toEqual("channel.2");
        });
    });
});

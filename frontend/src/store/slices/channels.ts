import { createEntityAdapter } from "@reduxjs/toolkit";
import { api } from "../../utils/links";
import { apiSlice } from "./api";
import type {
    Channel,
    ChannelDetailResponse,
    ChannelListResponse,
} from "../../types";
import type { EntityState } from "@reduxjs/toolkit";

const channelsAdapter = createEntityAdapter<Channel>({
    sortComparer: (first, second) => first.title.localeCompare(second.title),
});

const channelsApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listChannels: builder.query<EntityState<Channel, string>, void>({
            query: api.v1.channels.get,
            transformResponse: (response: ChannelListResponse) => {
                return channelsAdapter.addMany(
                    channelsAdapter.getInitialState(),
                    response,
                );
            },
        }),
        findChannelByHandle: builder.query<ChannelDetailResponse, string>({
            query: api.v1.channels.handle.get,
        }),
    }),
    overrideExisting: "throw",
});

export const { useListChannelsQuery, useFindChannelByHandleQuery } =
    channelsApi;

export const { selectAll: selectAllChannels } = channelsAdapter.getSelectors();

export const appendFetchedChannels = (channels: Channel[]) => {
    return channelsApi.util.updateQueryData(
        "listChannels",
        undefined,
        (draft) => {
            channelsAdapter.setMany(draft, channels);
        },
    );
};

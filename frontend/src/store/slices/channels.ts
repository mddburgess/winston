import { createEntityAdapter } from "@reduxjs/toolkit";
import { api } from "../../utils/links";
import { apiSlice } from "./api";
import type { ChannelDto } from "../../model/ChannelDto";
import type { EntityState } from "@reduxjs/toolkit";

const channelsAdapter = createEntityAdapter<ChannelDto>({
    sortComparer: (first, second) => first.title.localeCompare(second.title),
});

const channelsApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listChannels: builder.query<EntityState<ChannelDto, string>, void>({
            query: api.channels.get,
            transformResponse: (response: ChannelDto[]) => {
                return channelsAdapter.addMany(
                    channelsAdapter.getInitialState(),
                    response,
                );
            },
        }),
        findChannelByHandle: builder.query<ChannelDto, string>({
            query: api.channels.handle.get,
        }),
    }),
    overrideExisting: "throw",
});

export const { useListChannelsQuery, useFindChannelByHandleQuery } =
    channelsApi;

export const { selectAll: selectAllChannels } = channelsAdapter.getSelectors();

export const appendFetchedChannels = (channels: ChannelDto[]) => {
    return channelsApi.util.updateQueryData(
        "listChannels",
        undefined,
        (draft) => {
            channelsAdapter.setMany(draft, channels);
        },
    );
};

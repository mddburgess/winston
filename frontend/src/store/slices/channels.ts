import {apiSlice} from "./api";
import {createEntityAdapter, EntityState} from "@reduxjs/toolkit";
import {ChannelDto} from "../../model/ChannelDto";

export const channelsAdapter = createEntityAdapter<ChannelDto>({
    sortComparer: (first, second) => first.title.localeCompare(second.title),
})

const channelsApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listChannels: builder.query<EntityState<ChannelDto, string>, void>({
            query: () => "/channels",
            transformResponse: (response: ChannelDto[]) => {
                return channelsAdapter.addMany(channelsAdapter.getInitialState(), response);
            }
        }),
        findChannelById: builder.query<ChannelDto, string>({
            query: (channelId) => `/channels/${channelId}`
        }),
    }),
    overrideExisting: 'throw'
})

export const {
    useListChannelsQuery,
    useFindChannelByIdQuery,
} = channelsApi;

import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {ChannelDto} from "../../model/ChannelDto";

export const apiSlice = createApi({
    reducerPath: "api",
    baseQuery: fetchBaseQuery({baseUrl: "/api"}),
    endpoints: builder => ({
        listChannels: builder.query<ChannelDto[], void>({
            query: () => "/channels"
        })
    })
});

export const { useListChannelsQuery } = apiSlice

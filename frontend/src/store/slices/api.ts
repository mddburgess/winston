import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {ChannelDto} from "../../model/ChannelDto";
import {VideoDto} from "../../model/VideoDto";

export const apiSlice = createApi({
    reducerPath: "api",
    baseQuery: fetchBaseQuery({baseUrl: "/api"}),
    endpoints: builder => ({
        listChannels: builder.query<ChannelDto[], void>({
            query: () => "/channels"
        }),
        listVideosByChannelId: builder.query<VideoDto[], string>({
            query: (channelId) => `/channels/${channelId}/videos`
        })
    })
});

export const {
    useListChannelsQuery,
    useListVideosByChannelIdQuery,
} = apiSlice

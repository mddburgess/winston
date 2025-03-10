import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {ChannelDto} from "../../model/ChannelDto";
import {VideoWithChannelIdDto, VideoWithChannelDto} from "../../model/VideoDto";
import {CommentDto} from "../../model/CommentDto";

type FetchVideosRequest = {
    channelId: string;
    subscriptionId: string;
}

export const apiSlice = createApi({
    reducerPath: "api",
    baseQuery: fetchBaseQuery({baseUrl: "/api"}),
    endpoints: builder => ({
        listChannels: builder.query<ChannelDto[], void>({
            query: () => "/channels"
        }),
        listVideosByChannelId: builder.query<VideoWithChannelIdDto[], string>({
            query: (channelId) => `/channels/${channelId}/videos`
        }),
        findVideoById: builder.query<VideoWithChannelDto, string>({
            query: (videoId) => `/videos/${videoId}`
        }),
        listCommentsByVideoId: builder.query<CommentDto[], string>({
            query: (videoId) => `/videos/${videoId}/comments`
        }),
        listCommentsByAuthorId: builder.query<CommentDto[], string>({
            query: (authorId) => `/authors/${authorId}/comments`
        }),
        fetchVideosByChannelId: builder.mutation<VideoWithChannelIdDto, FetchVideosRequest>({
            query: (request) => ({
                url: `/channels/${request.channelId}/fetch-videos`,
                method: 'POST',
                headers: [ ["X-Notify-Subscription", request.subscriptionId] ]
            }),
        })
    })
});

export const {
    useListChannelsQuery,
    useListVideosByChannelIdQuery,
    useFindVideoByIdQuery,
    useListCommentsByVideoIdQuery,
    useListCommentsByAuthorIdQuery,
    useFetchVideosByChannelIdMutation,
} = apiSlice

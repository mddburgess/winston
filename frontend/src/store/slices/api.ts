import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {ChannelDto} from "../../model/ChannelDto";
import {VideoWithChannelIdDto, VideoWithChannelDto} from "../../model/VideoDto";
import {CommentDto} from "../../model/CommentDto";

type FetchRequest = {
    subscriptionId: string;
}

type FetchChannelRequest = FetchRequest & {
    channelHandle: string;
}

type FetchVideosRequest = FetchRequest & {
    channelId: string;
}

export const apiSlice = createApi({
    reducerPath: "api",
    baseQuery: fetchBaseQuery({baseUrl: "/api"}),
    endpoints: builder => ({
        listChannels: builder.query<ChannelDto[], void>({
            query: () => "/channels"
        }),
        findChannelById: builder.query<ChannelDto, string>({
            query: (channelId) => `/channels/${channelId}`
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
        fetchChannelByHandle: builder.mutation<string, FetchChannelRequest>({
            query: (request) => ({
                url: `/fetch`,
                method: `POST`,
                headers: [ ["X-Notify-Subscription", request.subscriptionId] ],
                body: {
                    channel: {
                        handle: request.channelHandle
                    }
                }
            })
        }),
        fetchVideosByChannelId: builder.mutation<VideoWithChannelIdDto, FetchVideosRequest>({
            query: (request) => ({
                url: `/fetch`,
                method: 'POST',
                headers: [ ["X-Notify-Subscription", request.subscriptionId] ],
                body: {
                    videos: {
                        channelId: request.channelId,
                        fetch: 'LATEST'
                    }
                }
            }),
        }),
    })
});

export const {
    useListChannelsQuery,
    useFindChannelByIdQuery,
    useListVideosByChannelIdQuery,
    useFindVideoByIdQuery,
    useListCommentsByVideoIdQuery,
    useListCommentsByAuthorIdQuery,
    useFetchChannelByHandleMutation,
    useFetchVideosByChannelIdMutation,
} = apiSlice

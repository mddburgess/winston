import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {ChannelDto} from "../../model/ChannelDto";
import {VideoWithChannelIdDto, VideoWithChannelDto} from "../../model/VideoDto";
import {CommentDto} from "../../model/CommentDto";
import {AuthorDetailsResponse} from "../../model/authors/AuthorDetailsResponse";
import {AuthorListResponse} from "../../model/authors/AuthorListResponse";

type FetchRequest = {
    subscriptionId: string;
}

type FetchChannelRequest = FetchRequest & {
    channelHandle: string;
}

type FetchVideosRequest = FetchRequest & {
    channelId: string;
    mode: 'ALL' | 'LATEST'
}

type FetchCommentsRequest = FetchRequest & {
    videoId: string;
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
        listAuthors: builder.query<AuthorListResponse, void>({
            query: () => "/authors"
        }),
        findAuthorDetailsById: builder.query<AuthorDetailsResponse, string>({
            query: (authorId) => `/authors/${authorId}`
        }),
        fetchChannelByHandle: builder.mutation<undefined, FetchChannelRequest>({
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
        fetchVideosByChannelId: builder.mutation<undefined, FetchVideosRequest>({
            query: (request) => ({
                url: `/fetch`,
                method: 'POST',
                headers: [ ["X-Notify-Subscription", request.subscriptionId] ],
                body: {
                    videos: {
                        channelId: request.channelId,
                        fetch: request.mode
                    }
                }
            }),
        }),
        fetchCommentsByVideoId: builder.mutation<undefined, FetchCommentsRequest>({
            query: (request) => ({
                url: `/fetch`,
                method: "POST",
                headers: [ ["X-Notify-Subscription", request.subscriptionId] ],
                body: {
                    comments: {
                        videoId: request.videoId
                    }
                }
            })
        })
    })
});

export const {
    useListChannelsQuery,
    useFindChannelByIdQuery,
    useListVideosByChannelIdQuery,
    useFindVideoByIdQuery,
    useListCommentsByVideoIdQuery,
    useListAuthorsQuery,
    useFindAuthorDetailsByIdQuery,
    useFetchChannelByHandleMutation,
    useFetchVideosByChannelIdMutation,
    useFetchCommentsByVideoIdMutation,
} = apiSlice

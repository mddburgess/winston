import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {ChannelDto} from "../../model/ChannelDto";
import {VideoWithChannelIdDto, VideoWithChannelDto} from "../../model/VideoDto";
import {CommentDto} from "../../model/CommentDto";
import {AuthorDetailsResponse} from "../../model/authors/AuthorDetailsResponse";
import {AuthorListResponse} from "../../model/authors/AuthorListResponse";
import {createEntityAdapter, EntityState} from "@reduxjs/toolkit";
import {ascBy, descBy} from "../../utils";
import {DateTime} from "luxon";

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

type FetchRepliesRequest = FetchRequest & {
    commentId: string;
}

export const channelsAdapter = createEntityAdapter<ChannelDto>({
    sortComparer: (first, second) => first.title.localeCompare(second.title),
})

export const videosAdapter = createEntityAdapter<VideoWithChannelIdDto>({
    sortComparer: descBy(video => DateTime.fromISO(video.publishedAt).valueOf()),
})

export const commentsAdapter = createEntityAdapter<CommentDto>({
    sortComparer: ascBy(comment => DateTime.fromISO(comment.publishedAt).valueOf()),
})

export const apiSlice = createApi({
    reducerPath: "api",
    baseQuery: fetchBaseQuery({baseUrl: "/api"}),
    endpoints: builder => ({
        listChannels: builder.query<EntityState<ChannelDto, string>, void>({
            query: () => "/channels",
            transformResponse: (response: ChannelDto[]) => {
                return channelsAdapter.addMany(channelsAdapter.getInitialState(), response);
            }
        }),
        findChannelById: builder.query<ChannelDto, string>({
            query: (channelId) => `/channels/${channelId}`
        }),
        listVideosByChannelId: builder.query<EntityState<VideoWithChannelIdDto, string>, string>({
            query: (channelId) => `/channels/${channelId}/videos`,
            transformResponse: (response: VideoWithChannelIdDto[]) => {
                return videosAdapter.addMany(videosAdapter.getInitialState(), response);
            }
        }),
        findVideoById: builder.query<VideoWithChannelDto, string>({
            query: (videoId) => `/videos/${videoId}`
        }),
        listCommentsByVideoId: builder.query<EntityState<CommentDto, string>, string>({
            query: (videoId) => `/videos/${videoId}/comments`,
            transformResponse: (response: CommentDto[]) => {
                return commentsAdapter.addMany(commentsAdapter.getInitialState(), response);
            }
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
        }),
        fetchRepliesByCommentId: builder.mutation<undefined, FetchRepliesRequest>({
            query: (request) => ({
                url: `/fetch`,
                method: "POST",
                headers: [ ["X-Notify-Subscription", request.subscriptionId] ],
                body: {
                    replies: {
                        commentId: request.commentId
                    }
                }
            })
        }),
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
    useFetchRepliesByCommentIdMutation,
    util: apiUtils
} = apiSlice

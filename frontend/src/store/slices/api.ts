import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {FetchLimits} from "../../model/FetchLimits";

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

export const apiSlice = createApi({
    reducerPath: "api",
    baseQuery: fetchBaseQuery({baseUrl: "/api"}),
    tagTypes: ["fetchLimits"],
    endpoints: builder => ({
        getFetchLimits: builder.query<FetchLimits, void>({
            query: () => `/fetch/limits`,
            providesTags: ["fetchLimits"],
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
        fetchRepliesByVideoId: builder.mutation<undefined, FetchCommentsRequest>({
            query: (request) => ({
                url: `/fetch`,
                method: "POST",
                headers: [ ["X-Notify-Subscription", request.subscriptionId] ],
                body: {
                    replies: {
                        videoId: request.videoId
                    }
                }
            })
        })
    })
});

export const {
    useGetFetchLimitsQuery,
    useFetchChannelByHandleMutation,
    useFetchVideosByChannelIdMutation,
    useFetchCommentsByVideoIdMutation,
    useFetchRepliesByCommentIdMutation,
    useFetchRepliesByVideoIdMutation,
} = apiSlice

export const invalidateFetchLimits = () => apiSlice.util.invalidateTags(["fetchLimits"]);

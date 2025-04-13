import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";

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
    endpoints: builder => ({
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
    useFetchChannelByHandleMutation,
    useFetchVideosByChannelIdMutation,
    useFetchCommentsByVideoIdMutation,
    useFetchRepliesByCommentIdMutation,
} = apiSlice

import { apiSlice } from "./api";
import type { EntityState } from "@reduxjs/toolkit";
import { createEntityAdapter } from "@reduxjs/toolkit";
import type {
    Video,
    VideoDetailsResponse,
    VideoListResponse,
} from "../../types";
import { descBy } from "../../utils";
import { DateTime } from "luxon";
import { api } from "../../utils/links";

const videosApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listVideosByChannelHandle: builder.query<
            EntityState<Video, string>,
            string
        >({
            query: api.v1.channels.handle.videos.get,
            transformResponse: (response: VideoListResponse) => {
                return videosAdapter.addMany(
                    videosAdapter.getInitialState(),
                    response,
                );
            },
        }),
        findVideoById: builder.query<VideoDetailsResponse, string>({
            query: api.v1.videos.id.get,
        }),
    }),
    overrideExisting: "throw",
});

export const {
    useListVideosByChannelHandleQuery,
    useFindVideoByIdQuery,
    util: videosApiUtils,
} = videosApi;

export const videosAdapter = createEntityAdapter<Video>({
    sortComparer: descBy((video) =>
        DateTime.fromISO(video.publishedAt).valueOf(),
    ),
});

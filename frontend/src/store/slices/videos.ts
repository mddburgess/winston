import { apiSlice } from "./api";
import { createEntityAdapter, EntityState } from "@reduxjs/toolkit";
import {
    VideoWithChannelDto,
    VideoWithChannelIdDto,
} from "../../model/VideoDto";
import { descBy } from "../../utils";
import { DateTime } from "luxon";

export const videosAdapter = createEntityAdapter<VideoWithChannelIdDto>({
    sortComparer: descBy((video) =>
        DateTime.fromISO(video.publishedAt).valueOf(),
    ),
});

const videosApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listVideosByChannelId: builder.query<
            EntityState<VideoWithChannelIdDto, string>,
            string
        >({
            query: (channelId) => `/channels/${channelId}/videos`,
            transformResponse: (response: VideoWithChannelIdDto[]) => {
                return videosAdapter.addMany(
                    videosAdapter.getInitialState(),
                    response,
                );
            },
        }),
        findVideoById: builder.query<VideoWithChannelDto, string>({
            query: (videoId) => `/videos/${videoId}`,
        }),
    }),
    overrideExisting: "throw",
});

export const {
    useListVideosByChannelIdQuery,
    useFindVideoByIdQuery,
    util: videosApiUtils,
} = videosApi;

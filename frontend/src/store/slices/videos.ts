import { createEntityAdapter } from "@reduxjs/toolkit";
import { DateTime } from "luxon";
import { descBy } from "../../utils";
import { api } from "../../utils/links";
import { apiSlice } from "./api";
import type {
    VideoWithChannelDto,
    VideoWithChannelIdDto,
} from "../../model/VideoDto";
import type { EntityState } from "@reduxjs/toolkit";

export const videosAdapter = createEntityAdapter<VideoWithChannelIdDto>({
    sortComparer: descBy((video) =>
        DateTime.fromISO(video.publishedAt).valueOf(),
    ),
});

const videosApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listVideosByChannelHandle: builder.query<
            EntityState<VideoWithChannelIdDto, string>,
            string
        >({
            query: api.channels.handle.videos.get,
            transformResponse: (response: VideoWithChannelIdDto[]) => {
                return videosAdapter.addMany(
                    videosAdapter.getInitialState(),
                    response,
                );
            },
        }),
        findVideoById: builder.query<VideoWithChannelDto, string>({
            query: api.videos.id.get,
        }),
    }),
    overrideExisting: "throw",
});

export const {
    useListVideosByChannelHandleQuery,
    useFindVideoByIdQuery,
    util: videosApiUtils,
} = videosApi;

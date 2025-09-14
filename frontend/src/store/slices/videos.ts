import { createEntityAdapter } from "@reduxjs/toolkit";
import { DateTime } from "luxon";
import { enhancedBackendApi } from "#/store/slices/backend";
import { descBy } from "#/utils";
import type { ListVideosResponse, Video } from "#/api";

const videosApi = enhancedBackendApi.enhanceEndpoints({
  endpoints: {
    listVideos: {
      transformResponse: (response: ListVideosResponse) =>
        videosAdapter.addMany(videosAdapter.getInitialState(), response.videos),
    },
  },
});

const { useListVideosQuery, useGetVideoQuery } = videosApi;

const videosAdapter = createEntityAdapter<Video>({
  sortComparer: descBy((video) => DateTime.fromISO(video.published_at).valueOf()),
});

const { selectAll: selectAllVideos } = videosAdapter.getSelectors();

export { selectAllVideos, useGetVideoQuery, useListVideosQuery };

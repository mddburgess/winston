import { createEntityAdapter } from "@reduxjs/toolkit";
import { DateTime } from "luxon";
import { enhancedBackendApi } from "#/store/slices/backend";
import { descBy } from "#/utils";
import type { ListVideosResponse, Video } from "#/api";
import type { EntityState } from "@reduxjs/toolkit";

const videosApi = enhancedBackendApi.enhanceEndpoints({
    endpoints: {
        listVideos: {
            transformResponse: (response: ListVideosResponse) =>
                videosAdapter.addMany(
                    videosAdapter.getInitialState(),
                    response.videos,
                ),
        },
    },
});

const { useListVideosQuery, useGetVideoQuery } = videosApi;

const videosAdapter = createEntityAdapter<Video>({
    sortComparer: descBy((video) =>
        DateTime.fromISO(video.published_at).valueOf(),
    ),
});

const { selectAll: selectAllVideos } = videosAdapter.getSelectors();

const appendVideos = (channelHandle: string, videos: Video[]) =>
    videosApi.util.updateQueryData(
        "listVideos",
        { handle: channelHandle },
        (draft: EntityState<Video, string>) => {
            videosAdapter.setMany(draft, videos);
        },
    );

const markVideoCommentsDisabled = (videoId: string) =>
    videosApi.util.updateQueryData(
        "getVideo",
        { id: videoId },
        (draft: Video) => {
            draft.comments = {
                comments_disabled: true,
                comment_count: 0,
                reply_count: 0,
                total_reply_count: 0,
                last_fetched_at: DateTime.now().toISO(),
            };
        },
    );

export {
    appendVideos,
    markVideoCommentsDisabled,
    selectAllVideos,
    useGetVideoQuery,
    useListVideosQuery,
};

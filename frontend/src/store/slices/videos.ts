import { createEntityAdapter } from "@reduxjs/toolkit";
import { DateTime } from "luxon";
import { enhancedBackendApi } from "#/store/slices/backend";
import { descBy } from "#/utils";
import type { ListVideosForChannelResp, Video } from "#/api";
import type { EntityState } from "@reduxjs/toolkit";

const videosApi = enhancedBackendApi.enhanceEndpoints({
    endpoints: {
        listVideosForChannel: {
            transformResponse: (response: ListVideosForChannelResp) =>
                videosAdapter.addMany(
                    videosAdapter.getInitialState(),
                    response.videos,
                ),
        },
    },
});

const { useListVideosForChannelQuery, useGetVideoByIdQuery } = videosApi;

const videosAdapter = createEntityAdapter<Video>({
    sortComparer: descBy((video) =>
        DateTime.fromISO(video.published_at).valueOf(),
    ),
});

const { selectAll: selectAllVideos } = videosAdapter.getSelectors();

const addVideosForChannel = (channelHandle: string, videos: Video[]) =>
    videosApi.util.updateQueryData(
        "listVideosForChannel",
        { handle: channelHandle },
        (draft: EntityState<Video, string>) => {
            videosAdapter.setMany(draft, videos);
        },
    );

const markVideoCommentsDisabled = (videoId: string) =>
    videosApi.util.updateQueryData(
        "getVideoById",
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
    addVideosForChannel,
    markVideoCommentsDisabled,
    selectAllVideos,
    useGetVideoByIdQuery,
    useListVideosForChannelQuery,
};
